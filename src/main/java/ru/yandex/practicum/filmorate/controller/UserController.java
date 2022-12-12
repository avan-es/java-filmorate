package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    //private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int idUserGenerator = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        User validUser = userValidation(user);
        validUser.setId(idUserGenerator);
        users.put(idUserGenerator++, validUser);
        return validUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user){
        if (!users.containsKey(user.getId())) {
            throw new UserValidationException("Пользователь с таким ID (" + user.getId() +
                    ") не найден.");
        }
        User validUser = userValidation(user);
        users.put(validUser.getId(), validUser);
        return validUser;
    }

    private User userValidation(User user){
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

}
