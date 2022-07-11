package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStorage implements UserStorage {
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
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    @Override
    public long getStartId() {
        return 1;
    }

    @Override
    public User addFriend(Long userId, Long friendId) throws UserNotFoundException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friend, FriendStatus.CONFIRMED);
        friend.addFriend(user, FriendStatus.UNCONFIRMED);
        return user;
    }

    @Override
    public Map<User, FriendStatus> getUserFriendsById(Long id) {
        return users.get(id).getFriends();
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        users.get(userId).getFriends().remove(friendId);
        return users.get(userId);
    }

}
