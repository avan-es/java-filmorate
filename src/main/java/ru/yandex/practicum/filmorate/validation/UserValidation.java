package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;


@RequiredArgsConstructor
@Component("userValidation")
public class UserValidation {

    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    public User userValidation(User user) {
        if (user.getEmail().isEmpty() ||
                user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new UserValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw  new UserValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidationException("Дата рождения не может быть в будущем.");
        }
        return user;
    }

    public void userIdValidation(int id) {
        if (!userStorage.getAllUsers().containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID: " + id +" не найден.");
        }
    }

}
