package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    public void setUp() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setBirthday(LocalDate.of(1900, 12, 1));
        user.setLogin("login");
        user.setEmail("email@email.com");
    }

    @Test
    public void shouldThrowValidationExceptionWhenEmptyEmail() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void shouldThrowValidationExceptionWhenEmailNotContainsAtSymbol() {
        user.setEmail("email%email.com");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void shouldThrowValidationExceptionWhenEmptyLogin() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void shouldThrowValidationExceptionWhenLoginHaveSpace() {
        user.setLogin("login with space");
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    public void shouldThrowValidationExceptionWhenBirthdayInTheFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}