package ru.yandex.practicum.filmorate.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.films.FilmTest.FILMS_ID_LIKE_USER_ID;
import static ru.yandex.practicum.filmorate.users.UserData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("Проверка получения событий по лайкам")
public class LikeEventTests extends EventTest {

    @Test
    void addLikeEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //проверка получения event по операции добавления лайка
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].eventId").exists())
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].entityId").value(filmId))
                .andExpect(jsonPath("$[0].eventType").value(EventType.LIKE.toString()))
                .andExpect(jsonPath("$[0].operation").value(OperationType.ADD.toString()))
                .andExpect(jsonPath("$[0].timestamp").exists());
    }

    @Test
    void deleteLikeEvent() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long filmId = getIdFromObject(createFilm(film));

        //добавление лайков и удаление лайка
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(delete(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());

        //проверка получения event по операции удаления лайка
        mockMvc.perform(get(USERS_ID_FEED, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].eventId").exists())
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].entityId").value(filmId))
                .andExpect(jsonPath("$[1].eventType").value(EventType.LIKE.toString()))
                .andExpect(jsonPath("$[1].operation").value(OperationType.REMOVE.toString()))
                .andExpect(jsonPath("$[1].timestamp").exists());
    }
}
