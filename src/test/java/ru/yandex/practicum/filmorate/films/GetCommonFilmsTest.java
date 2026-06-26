package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FilmRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.films.FilmData.film;
import static ru.yandex.practicum.filmorate.films.FilmData.film2;
import static ru.yandex.practicum.filmorate.users.UserData.*;

@DisplayName("Проверка общих фильмов")
public class GetCommonFilmsTest extends FilmTest {

    @Autowired
    public GetCommonFilmsTest(FilmRepository filmRepository) {
        super(filmRepository);
    }

    //проверка общих фильмов
    @Test
    public void GetCommonFilmsWhenFirstUserHaveAndSecondUserHave() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        Long filmId = getIdFromObject(createFilm(film));
        createFilm(film);
        createFilm(film);
        createFilm(film);

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, friendId))
                .andExpect(status().isOk());
        //проверка присутсвия лайков
        Assertions.assertTrue(filmRepository.hasLike(filmId, userId));
        Assertions.assertTrue(filmRepository.hasLike(filmId, friendId));

        mockMvc.perform(get(COMMON_FILMS)
                        .param("userId", userId.toString()).param("friendId", friendId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

        //очистка лайков
        filmRepository.deleteLike(filmId,userId);
        filmRepository.deleteLike(filmId,friendId);
    }

    //проверка на отсутствие общих фильмов
    @Test
    public void testWithOutCommonFilms() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        Long filmId = getIdFromObject(createFilm(film));
        Long filmId2 = getIdFromObject(createFilm(film2));

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId, userId))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, filmId2, friendId))
                .andExpect(status().isOk());

        mockMvc.perform(get(COMMON_FILMS)
                .param("userId", userId.toString()).param("friendId", friendId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    //проверка сортировки по популярности
    @Test
    public void testUnknownUser() throws Exception {
        Long idFilm1 = getIdFromObject(createFilm(film));
        Long idFilm2 = getIdFromObject(createFilm(film));
        Long idFilm3 = getIdFromObject(createFilm(film));
        Long idFilm4 = getIdFromObject(createFilm(film));
        Long idFilm5 = getIdFromObject(createFilm(film));

        Long idUser1 = getIdFromObject(createUser(user));
        Long idUser2 = getIdFromObject(createUser(user2));
        Long idUser3 = getIdFromObject(createUser(user3));

        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm1, idUser1))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm2, idUser1))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm3, idUser1))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm4, idUser1))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm3, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm2, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm5, idUser2))
                .andExpect(status().isOk());
        mockMvc.perform(put(FILMS_ID_LIKE_USER_ID, idFilm3, idUser3))
                .andExpect(status().isOk());

        mockMvc.perform(get(COMMON_FILMS)
                        .param("userId", idUser1.toString()).param("friendId", idUser2.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[1].id").value(2));
    }

}
