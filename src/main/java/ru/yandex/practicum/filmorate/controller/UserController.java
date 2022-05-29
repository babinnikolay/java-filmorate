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
        } else {
            log.error("user validation {}", user);
            throw new ValidationException();
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (isValid(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("update user {}", user);
        } else {
            log.error("user validation {}", user);
            throw new ValidationException();
        }
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    private boolean isValid(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        return !user.getEmail().isEmpty()
                && user.getEmail().contains("@")
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now());
    }
}
