package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        user.setId(nextId++);
        if (isValid(user)) {
            users.put(user.getId(), user);
            log.info("create new user {}", user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            String cause = "User id not found";
            log.error("user validation {} cause - ", user, cause);
            throw new ValidationException(cause);
        }
        if (isValid(user)) {
            users.put(user.getId(), user);
            log.info("update user {}", user);
        }
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    private boolean isValid(User user) throws ValidationException {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String cause;
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            cause = "Email is empty or not contains @";
            log.error("user validation {} cause - ", user, cause);
            throw new ValidationException(cause);
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            cause = "Login is empty or contains space";
            log.error("user validation {} cause - ", user, cause);
            throw new ValidationException(cause);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            cause = "birthday is after now";
            log.error("user validation {} cause - ", user, cause);
            throw new ValidationException(cause);
        }
        return true;
    }
}
