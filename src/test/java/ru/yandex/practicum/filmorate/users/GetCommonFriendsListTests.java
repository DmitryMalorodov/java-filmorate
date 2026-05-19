package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints.*;
import static ru.yandex.practicum.filmorate.users.UserData.user;

@DisplayName("Проверка получения общего списка друзей двух пользователей")
public class GetCommonFriendsListTests extends UserTest {

    @Test
    void checkGetCommonFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));
        Long friendId2 = getIdFromObject(createUser(user));

        //добавление в друзья
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,userId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID,friendId, friendId2))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS_ID_FRIENDS_COMMON_OTHER_ID, userId, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].email").value("email@em.ru"))
                .andExpect(jsonPath("$[0].login").value("Логин"))
                .andExpect(jsonPath("$[0].name").value("Имя"))
                .andExpect(jsonPath("$[0].birthday").value("2000-10-20"));
    }

    @Test
    void checkGetEmptyCommonFriendsList() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user));
        Long friendId2 = getIdFromObject(createUser(user));

        //добавление в друзья
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId, friendId2))
                .andExpect(status().isOk());

        mockMvc.perform(get(USERS_ID_FRIENDS_COMMON_OTHER_ID, userId, friendId2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
