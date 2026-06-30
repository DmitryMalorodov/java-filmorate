package ru.yandex.practicum.filmorate.users;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.users.UserData.user;
import static ru.yandex.practicum.filmorate.users.UserData.user2;

@DisplayName("Проверка получения списка друзей пользователя")
public class GetUserFriendsListTests extends UserTest {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public GetUserFriendsListTests(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        super(userRepository);
        this.friendshipRepository = friendshipRepository;
    }

    @Test
    void checkGetEmptyFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkGetFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья и подтверждение
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId, userId))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user2.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user2.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user2.getBirthday().toString()));
    }

    @Test
    void checkGetEmptyFriendsListDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Assertions.assertTrue(userRepository.getFriendsList(userId).isEmpty());
    }

    @Test
    void checkGetFriendsListDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья и подтверждение
        friendshipRepository.addFriend(userId, friendId);
        friendshipRepository.addFriend(friendId, userId);

        List<User> friends = userRepository.getFriendsList(userId);
        Assertions.assertEquals(1, friends.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(friends.getFirst(), user2, softAssert);
        softAssert.assertAll();
    }
}
