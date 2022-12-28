package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    //TZ 10
    private final UserService userService;
    //TZ 10
    private final UserValidation userValidation;

    @GetMapping
    //TZ 9
    public Collection<User> getAllUsers() {
        return userService.getAllUsers().values();
    }
    //TZ 10
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        userValidation.userIdValidation(id);
        return userService.getUserById(id);
    }
    //TZ 9
    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        userValidation.userValidation(user);
        return userService.addUser(user);
    }
    //TZ 9
    @PutMapping
    public User updateUser(@Valid @RequestBody User user){
        userValidation.userValidation(user);
        return userService.updateUser(user);
    }
    //TZ 10
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userValidation.userIdValidation(id);
        userValidation.userIdValidation(friendId);
        userService.addFriend(id, friendId);
    }
    //TZ 10
    @DeleteMapping ("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userValidation.userIdValidation(id);
        userValidation.userIdValidation(friendId);
        userService.deleteFriend(id, friendId);
    }

    //TZ 10
    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer id) {
        userValidation.userIdValidation(id);
        return userService.getUserFriends(id).values();
    }

    //TZ 10
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        userValidation.userIdValidation(id);
        userValidation.userIdValidation(otherId);
        return userService.getCommonFriends(id, otherId).values();
    }

}
