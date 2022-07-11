package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long id) throws UserNotFoundException;

    long getStartId();

    User addFriend(Long userId, Long friendId) throws UserNotFoundException;

    Map<User, FriendStatus> getUserFriendsById(Long id);

    User removeFriend(Long userId, Long friendId) throws UserNotFoundException;
}
