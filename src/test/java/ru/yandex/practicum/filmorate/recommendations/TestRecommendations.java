package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Тест рекомендованных фильмов")
public class TestRecommendations extends FilmTest {

    public static final String REST_REQUEST = "/users/{id}/recommendations";

    @Autowired
    public TestRecommendations(FilmRepository filmRepository) {
        super(filmRepository);
    }

    @Test
    public void testRecomendatetFilms() throws Exception {
        Long targetUserId = prepareTestData();

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void testWhenUserLikedALlFilms() throws Exception {
        Long targetUserId = prepareTestData();

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, 2, targetUserId));

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testWhenNewUserNotHaveLikedList() throws Exception {
        Long targetUserId = prepareTestData();
        //удаляю лайк с фильма теперь пользователь как будто только зарегестрировался
        mockMvc.perform(delete("/films/1/like/{idUser}", targetUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get(REST_REQUEST, targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    private Long prepareTestData() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long userId2 = getIdFromObject(createUser(user2));
        Long userId3 = getIdFromObject(createUser(user3));

        Long filmId = getIdFromObject(createFilm(film));
        Long filmId2 = getIdFromObject(createFilm(film));

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId3))
                .andExpect(status().isOk());

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, userId2))
                .andExpect(status().isOk());

        return userId3;
    }

}
