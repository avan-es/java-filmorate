package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Integer, User> users = new HashMap<>();

    private int idUserGenerator = 1;

    @Override
    public User addUser(User user) {
        user.setId(idUserGenerator);
        users.put(idUserGenerator++, user);
        return user;
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID: " + user.getId() +
                    " не найден.");
        }
        users.put(user.getId(), user);
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
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}
