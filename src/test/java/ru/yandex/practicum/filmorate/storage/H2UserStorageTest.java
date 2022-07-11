package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class H2UserStorageTest {

    private final UserStorage userStorage;

    @Test
    public void testFindUserById() throws UserNotFoundException {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testAddNewUser() throws UserNotFoundException {
        long id = 2;
        String email = "";
        String login = "";
        String name = "";
        LocalDate birthday = LocalDate.now();
        userStorage.addUser(new User(id, email, login, name, birthday));

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(2L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException {

        long id = 2;
        String email = "";
        String login = "";
        String name = "new name";
        LocalDate birthday = LocalDate.now();
        userStorage.updateUser(new User(id, email, login, name, birthday));

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(2L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "new name")
                );

    }

    @Test
    public void testGetAllUsers() {

        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.getAllUsers());

        assertThat(userOptional)
                .isPresent()
                .isNotEmpty();
    }

    @Test
    public void testUnknownUser() {

        assertThatThrownBy(() -> {
            User user = userStorage.getUserById(-1L);
        }).isInstanceOf(UserNotFoundException.class);

    }

    @Test
    public void testAddFriend() throws UserNotFoundException {

        userStorage.addFriend(1L, 2L);

        Optional<Map<User, FriendStatus>> userFriends = Optional.ofNullable(userStorage.getUserFriendsById(2L));

        assertThat(userFriends)
                .isPresent()
                .isNotEmpty();

    }

    @Test
    public void testRemoveFriend() throws UserNotFoundException {
        userStorage.removeFriend(1L, 2L);
        assertTrue("", userStorage.getUserFriendsById(1L).isEmpty());
    }

}