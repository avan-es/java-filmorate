package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserStorage userStorage;

    private final UserValidation userValidation;
    @Autowired
    public void setJdbcFilmDAO(@Qualifier("userDbStorage") UserDbStorage userDbStorage) {
        this.userStorage = userDbStorage;
    }

    public User addUser(User user) {
        userValidation.userValidation(user);
        return userStorage.addUser(user);
    }

    public User getUserById(Integer id) {
        userValidation.userIdValidationDB(id);
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        userValidation.isUserPresent(user);
        userValidation.userLoginIsBusy(user);
        return userStorage.updateUser(user);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public String addFriend (Integer id, Integer friendId){
        userValidation.isUserPresent(getUserById(id));
        userValidation.isUserPresent((getUserById(id)));
        return userStorage.addFriend(id, friendId);
    }

    public List<User> getUserFriends(Integer id) {
        userValidation.isUserPresent(getUserById(id));
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userValidation.isUserPresent(getUserById(id));
        userValidation.isUserPresent(getUserById(otherId));
        return userStorage.getCommonFriends(id, otherId);
    }

    public String deleteFriend(Integer id, Integer friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

}
