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
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка получения общего списка друзей двух пользователей")
public class GetCommonFriendsListTests extends UserTest {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public GetCommonFriendsListTests(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        super(userRepository);
        this.friendshipRepository = friendshipRepository;
    }

    @Test
    void checkGetCommonFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));
        Long friendId2 = getIdFromObject(createUser(user3));

        //добавление в друзья и подтверждение
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId2, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId2, friendId))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS_ID_FRIENDS_COMMON_OTHER_ID, userId, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value(user3.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user3.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user3.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user3.getBirthday().toString()));
    }

    @Test
    void checkGetEmptyCommonFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));
        Long friendId2 = getIdFromObject(createUser(user3));

        //добавление в друзья и подтверждение
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId2, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId2, friendId))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS_ID_FRIENDS_COMMON_OTHER_ID, userId, friendId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkGetCommonFriendsListDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));
        Long friendId2 = getIdFromObject(createUser(user3));

        //добавление в друзья и подтверждение
        friendshipRepository.addFriend(userId, friendId);
        friendshipRepository.addFriend(friendId, userId);
        friendshipRepository.addFriend(friendId, friendId2);
        friendshipRepository.addFriend(friendId2, friendId);

        List<User> friends = userRepository.getCommonFriendsList(userId, friendId2);
        Assertions.assertEquals(1, friends.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(friends.getFirst(), user2, softAssert);
        softAssert.assertAll();
    }
}
