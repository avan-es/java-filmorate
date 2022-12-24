package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();

    private int idUserGenerator = 1;

    @Override
    public User addUser(User user) {
        User validUser = userValidation(user);
        validUser.setId(idUserGenerator);
        users.put(idUserGenerator++, validUser);
        return validUser;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserValidationException("Пользователь с таким ID (" + user.getId() +
                    ") не найден.");
        }
        User validUser = userValidation(user);
        users.put(validUser.getId(), validUser);
        return validUser;
    }

    @Override
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

    @Override
    public User deleteUser(User user) {
        if (!users.containsKey((user.getId()))){
            throw new UserValidationException("Пользователь с почтой (" + user.getEmail() +
                    ") не найден.");
        }
        users.remove(user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
