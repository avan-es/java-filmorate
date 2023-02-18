package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserMapper implements ResultSetExtractor <List<User>>{
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, User> users = new LinkedHashMap<>();
        while (rs.next()){
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setBirthday(LocalDate.parse(rs.getString("birthday")));
            users.put(user.getId(), user);
        }
        return new ArrayList<>(users.values());
    }
}
