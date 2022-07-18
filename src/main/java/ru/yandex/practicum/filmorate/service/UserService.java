package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException, UserNotFoundException;

    Collection<User> getAllUsers();

    User getUserById(Long id) throws UserNotFoundException;

    User addFriend(Long id, Long friendId) throws UserNotFoundException;

    User removeFriend(Long id, Long friendId) throws UserNotFoundException;

    List<User> getAllFriends(Long id) throws UserNotFoundException;

    List<User> getCommonFriends(Long id, Long otherId);
}
