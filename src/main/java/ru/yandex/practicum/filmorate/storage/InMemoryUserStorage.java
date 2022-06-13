package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public boolean contains(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    @Override
    public Set<User> getUsersById(Set<Long> friendsId) {
        return users.values()
                .stream()
                .filter(user -> friendsId.contains(user.getId()))
                .collect(Collectors.toSet());
    }
}
