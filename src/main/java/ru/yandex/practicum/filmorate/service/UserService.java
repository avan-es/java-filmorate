package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend (Integer id, Integer friendId){
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(id);
    }

    public User deleteUser(User user) {
       return userStorage.deleteUser(user);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUser(id);
    }
    //TZ 10
    public void deleteFriend(Integer id, Integer friendId) {
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
    }

    public Map<Integer, User> getUserFriends(Integer id) {
        Map<Integer, User> friends = new HashMap<>();
        friends = userStorage.getUser(id).getFriends()
                .stream()
                .map(userStorage::getUser).collect(Collectors.toMap(User::getId, user -> user));
    return friends;
    }
}
