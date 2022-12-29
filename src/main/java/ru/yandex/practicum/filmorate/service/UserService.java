package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
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

    public List<User> getUserFriends(Integer id) {
        return userStorage.getUser(id).getFriends()
                .stream()
                .map(userStorage::getUser).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        Set<Integer> commonFriendsId = new HashSet<>(userStorage.getUser(id).getFriends());
        commonFriendsId.retainAll(userStorage.getUser(otherId).getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (int userId : commonFriendsId) {
            commonFriends.add(userStorage.getUser(userId));
        }
        return commonFriends;
    }
}
