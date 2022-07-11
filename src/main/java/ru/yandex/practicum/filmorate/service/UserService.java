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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private long userNextId;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
        userNextId = storage.getStartId();
    }

    public User addUser(User user) throws ValidationException {
        user.setId(userNextId);
        validation(user);
        storage.addUser(user);
        userNextId++;
        log.info("create new user {}", user);
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        validation(user);
        storage.updateUser(user);
        log.info("update user {}", user);
        return user;
    }

    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
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

    public User getUserById(Long id) throws UserNotFoundException {
        return storage.getUserById(id);
    }

    public User addFriend(Long id, Long friendId) throws UserNotFoundException {
        return storage.addFriend(id, friendId);
    }

    public User removeFriend(Long id, Long friendId) throws UserNotFoundException {
        User user = storage.getUserById(id);
        storage.removeFriend(id, friendId);
        return user;
    }

    public List<User> getAllFriends(Long id) throws UserNotFoundException {
        User user = getUserById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return List.copyOf(storage.getUserFriendsById(id).keySet());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<User> friends = storage.getUserFriendsById(id).keySet();
        Set<User> otherFriends = storage.getUserFriendsById(otherId).keySet();
        return friends
                .stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }
}
