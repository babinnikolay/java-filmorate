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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private long nextId = 1;
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User addUser(User user) throws ValidationException {
        user.setId(nextId);
        if (isValid(user)) {
            storage.addUser(user);
            nextId++;
            log.info("create new user {}", user);
        }
        return user;
    }

    public User updateUser(User user) throws ValidationException, UserNotFoundException {
        if (!storage.contains(user)) {
            String cause = "User id not found";
            log.error("user validation {} cause - {}", user, cause);
            throw new UserNotFoundException(cause);
        }
        if (isValid(user)) {
            storage.updateUser(user);
            log.info("update user {}", user);
        }
        return user;
    }

    public Collection<User> getUsers() {
        return storage.getUsers();
    }

    private boolean isValid(User user) throws ValidationException {
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
        return true;
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return storage.getUserById(id);
    }

    public User addFriend(Long id, Long friendId) throws UserNotFoundException {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.addFriend(friend);
        friend.addFriend(user);
        return user;
    }

    public User removeFriend(Long id, Long friendId) throws UserNotFoundException {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.removeFriend(friend);
        friend.removeFriend(user);
        return user;
    }

    public Set<User> getAllFriends(Long id) throws UserNotFoundException {
        User user = storage.getUserById(id);
        Set<Long> friendsId = user.getFriends();
        return storage.getUsersById(friendsId);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) throws UserNotFoundException {
        Set<Long> friends = storage.getUserById(id).getFriends();
        Set<Long> otherFriends = storage.getUserById(otherId).getFriends();
        Set<Long> commonFriends = friends
                .stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toSet());
        return storage.getUsersById(commonFriends);
    }
}
