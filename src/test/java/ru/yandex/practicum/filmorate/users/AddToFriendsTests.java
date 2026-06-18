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
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка добавления в список друзей")
public class AddToFriendsTests extends UserTest {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public AddToFriendsTests(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        super(userRepository);
        this.friendshipRepository = friendshipRepository;
    }

    @Test
    void checkAddToFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //подтверждение добавления в друзья от friendId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId, userId))
                .andExpect(status().isOk());

        //проверка что у юзера с userId появился друг с friendId
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user2.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user2.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user2.getBirthday().toString()));

        //проверка что у юзера с friendId появился друг с userId
        mockMvc.perform(get(USERS_ID_FRIENDS, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user.getBirthday().toString()));
    }

    @Test
    void checkAddToFriendsWithoutConfirmation() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //проверка что у юзера с userId пустой список друзей
        mockMvc.perform(get(USERS_ID_FRIENDS, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkAddTwoFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));
        Long friendId2 = getIdFromObject(createUser(user3));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId2))
                .andExpect(status().isOk());

        //подтверждение добавления в друзья от friendId, friendId2
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId2, userId))
                .andExpect(status().isOk());

        //проверка что у юзера с userId появился друг с friendId
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[0].login").value(user2.getLogin()))
                .andExpect(jsonPath("$[0].name").value(user2.getName()))
                .andExpect(jsonPath("$[0].birthday").value(user2.getBirthday().toString()));

        //проверка что у юзера с userId появился друг с friendId2
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].email").value(user3.getEmail()))
                .andExpect(jsonPath("$[1].login").value(user3.getLogin()))
                .andExpect(jsonPath("$[1].name").value(user3.getName()))
                .andExpect(jsonPath("$[1].birthday").value(user3.getBirthday().toString()));
    }

    @Test
    void checkEmptyFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));

        //проверка что у юзера с userId пустой список друзей
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void checkAddFriendWithDoesNotExistId() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendNotExistId = 4L;

        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, friendNotExistId)));
    }

    @Test
    void checkAddFriendWithDoesNotExistId2() throws Exception {
        Long userNotExistId = 2L;
        Long friendNotExistId = getIdFromObject(createUser(user));

        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userNotExistId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userNotExistId)));
    }

    @Test
    void checkAddToFriendsDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        friendshipRepository.addFriend(userId, friendId);
        //подтверждение добавления в друзья от friendId
        friendshipRepository.addFriend(friendId, userId);

        List<User> friends = userRepository.getFriendsList(userId);
        Assertions.assertEquals(1, friends.size());

        SoftAssertions softAssert = new SoftAssertions();
        checkUser(friends.getFirst(), user2, softAssert);
        softAssert.assertAll();
    }

    @Test
    void checkAddToFriendsWithoutConfirmationDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        friendshipRepository.addFriend(userId, friendId);

        Assertions.assertTrue(userRepository.getFriendsList(friendId).isEmpty());
    }

    @Test
    void checkEmptyFriendsListDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Assertions.assertTrue(userRepository.getFriendsList(userId).isEmpty());
    }
}
