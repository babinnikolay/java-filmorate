package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<User> friends = getUserFriendsById(id).keySet();
        Set<User> otherFriends = getUserFriendsById(otherId).keySet();
        return friends
                .stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }

}
