package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    UserStorage inMemoryUserStorage;

    public UserController(UserStorage userStorage) {
        this.inMemoryUserStorage = userStorage;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        return inMemoryUserStorage.updateUser(user);
    }

}
