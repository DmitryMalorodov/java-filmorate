package ru.yandex.practicum.filmorate.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.users.UserData.user;
import static ru.yandex.practicum.filmorate.users.UserData.user2;
import static ru.yandex.practicum.filmorate.users.UserTest.USERS_ID_FRIENDS_FRIEND_ID;

@DisplayName("Проверка получения событий по лайкам")
public class FriendEventTests extends EventTest {

    @Test
    void addFriendEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //проверка получения event по операции добавления в друзья
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].eventId").exists())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].entityId").value(friendId))
                .andExpect(jsonPath("$[0].eventType").value(EventType.FRIEND.toString()))
                .andExpect(jsonPath("$[0].operation").value(OperationType.ADD.toString()))
                .andExpect(jsonPath("$[0].timestamp").exists());
    }

    @Test
    void deleteFriendEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());
        //удаление из друзей
        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //проверка получения event по операции удаления из друзей
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].entityId").value(friendId))
                .andExpect(jsonPath("$[1].eventType").value(EventType.FRIEND.toString()))
                .andExpect(jsonPath("$[1].operation").value(OperationType.REMOVE.toString()))
                .andExpect(jsonPath("$[1].timestamp").exists());
    }
}
