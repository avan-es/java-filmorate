package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.AddToFriendsException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY)" +
                            "VALUES (?, ?, ?, ?)",
                    new String[]{"USER_ID"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        String sqlRequest =  "select * " +
                "from PUBLIC.USERS where USER_ID = " + id;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if(userRows.next()) {
            User user = new User(id,
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getString("EMAIL"),
                    LocalDate.parse(userRows.getString("BIRTHDAY")));
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с ID %d не найден.", id));
        }
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        String sqlRequest = "UPDATE USERS SET LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlRequest, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        String sqlRequest =  "select * from PUBLIC.USERS";
        List<User> users =  jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeUser(rs));
        HashMap<Integer, User> usersMap = new HashMap<>();
        for (User user: users) {
            usersMap.put(user.getId(), user);
        }
        return usersMap;
    }

    @Override
    public String addFriend(Integer fromUserId, Integer toUserId){
        if (fromUserId.equals(toUserId)) {
            throw new AddToFriendsException("Нельзя добавить себя к себе в друзья.");
        }
        getUserById(fromUserId);
        getUserById(toUserId);
        String sqlFrom = "SELECT * FROM friends WHERE from_user = ? AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, toUserId, fromUserId);
        if (userRows.next()) {
            String sql = "UPDATE friends SET status = ? WHERE from_user = ? AND to_user = ?";
            jdbcTemplate.update(sql, "accepted", toUserId, fromUserId);
            return String.format("Теперь вы и пользователь с ID %d друзья.", toUserId);
        }
        userRows = jdbcTemplate.queryForRowSet(sqlFrom, fromUserId, toUserId);
        if (userRows.next()){
            throw new AddToFriendsException("Запрос на добавление в друзья уже существует.");
        }
        jdbcTemplate.update("INSERT INTO friends (from_user, to_user) VALUES (?, ?)", fromUserId, toUserId);
        return String.format("Запрос на добавление в друзь пользователю с ID %d отправлен.", toUserId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlFrom = "SELECT TO_USER FROM FRIENDS WHERE FROM_USER =" + id;
        String sqlTo = "SELECT FROM_USER FROM FRIENDS WHERE TO_USER =" + id + " AND STATUS = 'accepted'";
        List<Integer> idsFrom = jdbcTemplate.queryForList(sqlFrom, Integer.class);
        List<Integer> idsTo = jdbcTemplate.queryForList(sqlTo, Integer.class);
        List<Integer> allFriends = Stream.concat(idsFrom.stream(), idsTo.stream()).distinct().collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (Integer i : allFriends) {
            users.add(getUserById(i));
        }
        return users;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = getFriends(id).stream()
                .filter(user -> getFriends(otherId).contains(user))
                .collect(Collectors.toList());
        return commonFriends;
    }

    @Override
    public String deleteFriend(Integer id, Integer friendId) {
        getUserById(id);
        getUserById(friendId);
        String sqlFrom = "SELECT * FROM friends WHERE from_user = ? AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, id, friendId);
        if (userRows.next()) {
            jdbcTemplate.update("DELETE FROM friends WHERE from_user = ? AND to_user = ?", id, friendId);
            return String.format("Пользователь с идентификатором %d удален из друзей пользователя с идентификатором %d.", friendId, id);
        } else {
            throw new UserValidationException("Нельзя удалить пользователя из друзей, если он не является вашим другом.");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("USER_ID"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getString("EMAIL"),
                LocalDate.parse(rs.getString("BIRTHDAY")));
        return user;
    }
}
