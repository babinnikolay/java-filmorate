package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    Collection<User> getUsers();

    boolean contains(User user);

    User getUserById(Long id) throws UserNotFoundException;

    Set<User> getUsersById(Set<Long> friendsId);
}
