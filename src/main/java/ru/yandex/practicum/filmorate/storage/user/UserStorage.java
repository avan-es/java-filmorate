package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Integer, User> getAllUsers();
    User addUser(User user);
    User getUserById(Integer id);
    User updateUser(User user);
    String addFriend(Integer id, Integer friendId);
    List<User> getFriends(Integer id);
    List<User> getCommonFriends(Integer id, Integer otherId);
    String deleteFriend(Integer id, Integer friendId);
//    void deleteUser(Integer id);
}
