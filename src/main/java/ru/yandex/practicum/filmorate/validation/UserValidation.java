package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.ModelValidationException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;


@Component("userValidation")
public class UserValidation {

    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserValidation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User userValidation(User user) {
        if (user.getEmail().isEmpty() ||
                user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ModelValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw  new ModelValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ModelValidationException("Дата рождения не может быть в будущем.");
        }
        return user;
    }

    public void userIdValidation(int id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден.", id));
        }
    }

    public void userIdValidationDB(int id) {
        String sqlRequest = "SELECT user_id " +
                            "FROM users " +
                            "WHERE user_id = " + id +
                            " FETCH FIRST 1 ROWS ONLY";
        if (!jdbcTemplate.queryForRowSet(sqlRequest).next()) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден.", id));
        }
    }

    public void userLoginIsBusy (User user) {
        String sqlRequest = "SELECT login " +
                            "FROM users " +
                            "WHERE login = " + user.getLogin() +
                            " FETCH FIRST 1 ROWS ONLY";
        try {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlRequest);
            if (userRows.next()) {
                throw new ModelValidationException("Логин: " + user.getLogin() +" уже занят.");
            }
        } catch (Exception e){

        }
    }

    public void userEmailIsBusy (User user) {
        String sqlRequest =  "select EMAIL " +
                "from PUBLIC.USERS where EMAIL = " + user.getEmail();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if (userRows.next()) {
            throw new ModelValidationException("Почта: " + user.getEmail() +" уже занята.");
        }
    }

    public boolean isUserPresent(User user){
        String sqlRequest =  "select USER_ID " +
                "from PUBLIC.USERS where USER_ID = " + user.getId() + " FETCH FIRST 1 ROWS ONLY";
        try {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlRequest);
            if (!userRows.next()) {
                throw new NotFoundException(String.format("Пользователь с ID %d не найден.", user.getId()));
            }
            return true;
        } catch (Exception e){
            throw new NotFoundException(String.format("Пользователь с ID %d не найден.", user.getId()));
        }
    }

}
