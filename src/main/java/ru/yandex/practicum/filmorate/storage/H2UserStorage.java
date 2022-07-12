package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class H2UserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2UserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {
        String queryUser = "INSERT INTO FILMORATE_USER" +
                "(user_id, email, login, name, birthday) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(queryUser,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE filmorate_user " +
                "SET " +
                "email=?, " +
                "login=?, " +
                "name=?, " +
                "birthday=? " +
                "WHERE user_id=?";
        jdbcTemplate.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        String query = "SELECT * FROM filmorate_user";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeUser(rs.getLong("user_id"), rs));
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        String userQuery = "SELECT * FROM filmorate_user WHERE user_id=?";
        List<User> users = jdbcTemplate.query(userQuery, (rs, rowNum) -> makeUser(id, rs), id);
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users.get(0);
    }

    @Override
    public long getStartId() {
        String query = "SELECT MAX(user_id) AS max_user_id FROM filmorate_user";
        Long maxUserId = jdbcTemplate.queryForObject(query, Long.class);
        if (maxUserId == null) {
            maxUserId = 0L;
        }
        return maxUserId + 1;
    }

    @Override
    public User addFriend(Long userId, Long friendId) throws UserNotFoundException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user == null || friend == null) {
            throw new UserNotFoundException();
        }
        String query = "INSERT INTO user_friend " +
                "(user_id, friend_id, friend_status_id) " +
                "VALUES(?, ?, ?)";
        jdbcTemplate.update(query, userId, friendId, FriendStatus.CONFIRMED.ordinal());
        jdbcTemplate.update(query, friendId, userId, FriendStatus.UNCONFIRMED.ordinal());
        return getUserById(userId);
    }

    public Map<User, FriendStatus> getUserFriendsById(Long id) {
        String query =
                "SELECT " +
                        "u.user_id, " +
                        "email, " +
                        "login, " +
                        "name, " +
                        "birthday, " +
                        "uf.friend_status_id " +
                        "FROM FILMORATE_USER u " +
                        "INNER JOIN (SELECT friend_id, user_id, friend_status_id " +
                        " FROM user_friend " +
                        " WHERE user_id = ? AND friend_status_id = 0) AS uf " +
                        " ON u.user_id = uf.friend_id";

        Map<User, FriendStatus> friends = new HashMap<>();
        jdbcTemplate.query(query, rs -> {
            do {
                User friend = makeUser(rs.getLong("user_id"), rs);
                FriendStatus fs = FriendStatus.values()[rs.getInt("friend_status_id")];
                friends.put(friend, fs);
            } while (rs.next());
        }, id);
        return friends;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) throws UserNotFoundException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user == null || friend == null) {
            throw new UserNotFoundException();
        }

        String query = "DELETE FROM USER_FRIEND WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(query, userId, friendId);
        jdbcTemplate.update(query, friendId, userId);
        return user;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String query = "SELECT * FROM FILMORATE_USER WHERE USER_ID IN (" +
                " SELECT U.FRIEND_ID FROM USER_FRIEND AS U" +
                " JOIN (SELECT F.* FROM USER_FRIEND AS F WHERE USER_ID = ?) AS F" +
                " WHERE U.USER_ID = ?" +
                "   AND U.FRIEND_ID = F.FRIEND_ID" +
                " GROUP BY U.FRIEND_ID)";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeUser(rs.getLong("user_id"), rs), id, otherId);
    }

    private User makeUser(long userId, ResultSet rs) throws SQLException {

        String email = rs.getString("email");
        String name = rs.getString("name");
        String login = rs.getString("login");
        LocalDate birthDay = rs.getDate("birthday").toLocalDate();

        return new User(userId, email, login, name, birthDay);
    }
}
