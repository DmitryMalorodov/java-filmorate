package ru.yandex.practicum.filmorate.films;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;

public class FilmData {
    static final Film film = Film.builder()
            .name("Имя фильма")
            .description("Описание")
            .releaseDate(LocalDate.of(2000, 12, 25))
            .duration(145)
            .mpaId(1)
            .build();

    static final Film film2 = Film.builder()
            .name("Имя фильма2")
            .description("Описание2")
            .releaseDate(LocalDate.of(2000, 12, 20))
            .duration(100)
            .mpaId(2)
            .build();

    static final String STR_200_LENGTH = "Описание200символов!Описание200символов!" +
            "Описание200символов!Описание200символов!Описание200символов!Описание200символов!" +
            "Описание200символов!Описание200символов!Описание200символов!Описание200символов!";

    static final String STR_201_LENGTH = "Описание201символов!Описание201символов!" +
            "Описание201символов!Описание201символов!Описание201символов!Описание201символов!" +
            "Описание201символов!Описание201символов!Описание201символов!Описание201символов!!";

    static final LocalDate TOO_SOON_RELEASE_DATE = LocalDate.of(1895, 12, 27);
    static final LocalDate CORRECT_RELEASE_DATE = LocalDate.of(1895, 12, 28);
}
