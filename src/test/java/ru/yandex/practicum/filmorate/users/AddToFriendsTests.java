package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints.*;
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка добавления в список друзей")
public class AddToFriendsTests extends UserTest {

    @Test
    void checkAddToFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));

        //добавление в друзья
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //проверка что у юзера с userId появился друг с friendId
        mockMvc.perform(get(USERS_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds[0]").value(2));

        //проверка что у юзера с friendId появился друг с userId
        mockMvc.perform(get(USERS_ID, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds[0]").value(1));
    }

    @Test
    void checkAddTwoFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));
        Long friendId2 = getIdFromObject(createUser(user));

        //добавление в друзья
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendId))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId2))
                .andExpect(status().isOk());

        //проверка что у юзера с userId появился друг с friendId
        mockMvc.perform(get(USERS_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds[0]").value(2));

        //проверка что у юзера с userId появился друг с friendId2
        mockMvc.perform(get(USERS_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds[1]").value(3));
    }

    @Test
    void checkEmptyFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));

        //проверка что у юзера с userId пустой список друзей
        mockMvc.perform(get(USERS_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds").isEmpty());

        //проверка что у юзера с friendId пустой список друзей
        mockMvc.perform(get(USERS_ID, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.friendsIds").isEmpty());
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
}
