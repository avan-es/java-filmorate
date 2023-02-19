package ru.yandex.practicum.filmorate.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.user.UserMapper;
import ru.yandex.practicum.filmorate.exeptions.AddToFriendsException;
import ru.yandex.practicum.filmorate.exeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;

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
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, name, email, birthday)" +
                                                                   "VALUES (?, ?, ?, ?)",
                    new String[]{"user_id"});
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
        String sqlRequest =  "SELECT * " +
                             "FROM users WHERE user_id = " + id;
        List<User> user = jdbcTemplate.query(sqlRequest, new UserMapper());
        return user.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId());
        String sqlRequest = "UPDATE users " +
                            "SET login = ?, name = ?, email = ?, birthday = ? " +
                            "WHERE user_id = ?";
        jdbcTemplate.update(sqlRequest, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(),
                            user.getId());
        return getUserById(user.getId());
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        String sqlRequest =  "SELECT * " +
                             "FROM users";
        List<User> users = jdbcTemplate.query(sqlRequest, new UserMapper());
        return users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
    }

    @Override
    public String addFriend(Integer fromUserId, Integer toUserId){
        if (fromUserId.equals(toUserId)) {
            throw new AddToFriendsException("Нельзя добавить себя к себе в друзья.");
        }
        getUserById(fromUserId);
        getUserById(toUserId);
        String sqlFrom = "SELECT * " +
                         "FROM friends " +
                         "WHERE from_user = ? " +
                         "AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, toUserId, fromUserId);
        if (userRows.next()) {
            String sql = "UPDATE friends " +
                         "SET status = ? " +
                         "WHERE from_user = ? " +
                         "AND to_user = ?";
            jdbcTemplate.update(sql, "Accepted", toUserId, fromUserId);
            return String.format("Теперь вы и пользователь с ID %d друзья.", toUserId);
        }
        userRows = jdbcTemplate.queryForRowSet(sqlFrom, fromUserId, toUserId);
        if (userRows.next()){
            throw new AddToFriendsException("Запрос на добавление в друзья уже существует.");
        }
        jdbcTemplate.update("INSERT INTO friends (from_user, to_user) " +
                                "VALUES (?, ?)", fromUserId, toUserId);
        return String.format("Запрос на добавление в друзь пользователю с ID %d отправлен.", toUserId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlFrom = "SELECT to_user " +
                         "FROM friends " +
                         "WHERE from_user =" + id;
        String sqlTo = "SELECT from_user " +
                       "FROM friends " +
                       "WHERE to_user = " + id +
                       " AND status = 'Accepted'";
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
        String sqlFrom = "SELECT * " +
                         "FROM friends " +
                         "WHERE from_user = ? " +
                         "AND to_user = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlFrom, id, friendId);
        if (userRows.next()) {
            jdbcTemplate.update("DELETE FROM friends " +
                                    "WHERE from_user = ? " +
                                    "AND to_user = ?", id, friendId);
            return String.format("Пользователь с идентификатором %d удален из друзей пользователя с идентификатором %d.", friendId, id);
        } else {
            throw new UserValidationException("Нельзя удалить пользователя из друзей, если он не является вашим другом.");
        }
    }

}
