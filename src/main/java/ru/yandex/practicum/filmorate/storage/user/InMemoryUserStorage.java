package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

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
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден.", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    public User deleteUser(User user) {
        if (!users.containsKey((user.getId()))){
            throw new NotFoundException("Пользователь с почтой (" + user.getEmail() +
                    ") не найден.");
        }
        users.remove(user.getId());
        return user;
    }

    @Override
    public String addFriend(Integer id, Integer friendId) {
        users.get(id).getFriends().add(friendId);
        return String.format("Пользователи с ID %d и %d теперь друзья!", id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        return users.get(id).getFriends()
                .stream()
                .map(users::get).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        Set<Integer> commonFriendsId = new HashSet<>(users.get(id).getFriends());
        commonFriendsId.retainAll(users.get(otherId).getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (int userId : commonFriendsId) {
            commonFriends.add(users.get(userId));
        }
        return commonFriends;
    }

    @Override
    public String deleteFriend(Integer id, Integer friendId) {
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
        return String.format("Пользователи с ID %d и %d больше не друзья.", id, friendId);
    }

//    //TODO реализовать метод
//    @Override
//    public void deleteUser(Integer id) {
//
//    }


    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }
}
