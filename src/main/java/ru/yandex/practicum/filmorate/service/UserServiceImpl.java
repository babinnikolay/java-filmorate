package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private long userNextId;

    @Autowired
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
        userNextId = storage.getStartId();
    }

    @Override
    public User addUser(User user) throws ValidationException {
        user.setId(userNextId);
        validation(user);
        storage.addUser(user);
        userNextId++;
        log.info("create new user {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException, UserNotFoundException {
        validation(user);
        if (storage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException();
        }
        storage.updateUser(user);
        log.info("update user {}", user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return storage.getUserById(id);
    }

    @Override
    public User addFriend(Long id, Long friendId) throws UserNotFoundException {
        return storage.addFriend(id, friendId);
    }

    @Override
    public User removeFriend(Long id, Long friendId) throws UserNotFoundException {
        User user = storage.getUserById(id);
        storage.removeFriend(id, friendId);
        return user;
    }

    @Override
    public List<User> getAllFriends(Long id) throws UserNotFoundException {
        User user = getUserById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return List.copyOf(storage.getUserFriendsById(id).keySet());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return storage.getCommonFriends(id, otherId);
    }

    private void validation(User user) throws ValidationException {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String cause;
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            cause = "Email is empty or not contains @";
            log.error("user validation {} cause - {}", user, cause);
            throw new ValidationException(cause);
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            cause = "Login is empty or contains space";
            log.error("user validation {} cause - {}", user, cause);
            throw new ValidationException(cause);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            cause = "birthday is after now";
            log.error("user validation {} cause - {}", user, cause);
            throw new ValidationException(cause);
        }
    }
}
