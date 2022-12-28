package ru.yandex.practicum.filmorate.storage.user;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getAllUsers();

    User addUser(User user);

    User getUser(int id);  //TZ 10

    User updateUser(User user);

    User deleteUser (User user); //TZ 10

}
