package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints.*;
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка удаления из списока друзей")
public class DeleteFromFriendsTests extends UserTest {

    @Test
    void checkDeleteFromFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));

        //добавление в друзья
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //удаление из друзей
        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

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
    void checkDeleteFriendWithDoesNotExistId() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendNotExistId = 4L;

        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, friendNotExistId)));
    }

    @Test
    void checkDeleteFriendWithDoesNotExistId2() throws Exception {
        Long userNotExistId = 2L;
        Long friendNotExistId = getIdFromObject(createUser(user));

        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userNotExistId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userNotExistId)));
    }
}
