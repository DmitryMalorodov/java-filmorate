package ru.yandex.practicum.filmorate.films;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.*;
import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.DURATION_MUST_BE_POSITIVE_MESSAGE;
import static ru.yandex.practicum.filmorate.films.FilmData.*;

@DisplayName("Проверка изменения фильмов")
public class PutFilmTests extends FilmTest {

    @Test
    void checkChangeFilm() throws Exception {
        Film newFilm = prepareReqBody(film);

        changeFilm(newFilm)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newFilm.getId()))
                .andExpect(jsonPath("$.name").value("Имя фильма"))
                .andExpect(jsonPath("$.description").value("Описание"))
                .andExpect(jsonPath("$.releaseDate").value("2000-12-25"))
                .andExpect(jsonPath("$.duration").value(145));
    }

    @Test
    void checkNameNullValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setName(null);

        changeFilm(newFilm)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Имя фильма"));
    }

    @Test
    void checkNameBlankValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setName(" ");

        changeFilm(newFilm)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Имя фильма"));
    }

    @Test
    void checkDescriptionNullValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDescription(null);

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescriptionBlankValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDescription(" ");

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription200LengthValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDescription(STR_200_LENGTH);

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDescription201LengthValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDescription(STR_201_LENGTH);

        checkValidationError(changeFilm(newFilm), DESCRIPTION_MAX_LENGTH_MESSAGE);
    }

    @Test
    void checkReleaseDateTooSoonValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setReleaseDate(TOO_SOON_RELEASE_DATE);

        checkValidationError(changeFilm(newFilm), RELEASE_DATE_MIN_MESSAGE);
    }

    @Test
    void checkReleaseDateCorrectValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setReleaseDate(CORRECT_RELEASE_DATE);

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkDurationNegativeValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDuration(-1);

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationZeroValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDuration(0);

        checkValidationError(changeFilm(newFilm), DURATION_MUST_BE_POSITIVE_MESSAGE);
    }

    @Test
    void checkDurationPositiveValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setDuration(1);

        changeFilm(newFilm)
                .andExpect(status().isOk());
    }

    @Test
    void checkIdNullValidation() throws Exception {
        Film newFilm = prepareReqBody(film);
        newFilm.setId(null);

        checkValidationError(changeFilm(newFilm), ID_NULL_MESSAGE);
    }

    private Film prepareReqBody(Film film) throws Exception {
        return film.toBuilder()
                .id(getIdFromObject(createFilm(film)))
                .build();
    }
}
