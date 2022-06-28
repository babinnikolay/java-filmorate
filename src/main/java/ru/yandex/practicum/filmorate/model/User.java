package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private Long id;

    private Map<Long, FriendStatus> friends = new HashMap<>();

    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public void addFriend(User friend) {
        friends.put(friend.getId(), FriendStatus.UNCONFIRMED);
    }

    public void removeFriend(User friend) {
        friends.remove(friend.getId());
    }
}
