package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Helper.getNextId;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final int MAX_DESC_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);
        film.setId(getNextId(films.keySet()));
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        validate(newFilm);

        Film oldFilm = films.get(newFilm.getId());
        log.info("Фильм для редактирования {}", oldFilm);
        if (oldFilm != null) {
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Отредактированный фильм {}", oldFilm);
            return oldFilm;
        }

        log.info("Фильм с id {} отсутствует", newFilm.getId());
        throw new ValidationException(String.format("Фильм с id '%d' отсутствует", newFilm.getId()));
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > MAX_DESC_LENGTH) {
            log.info("Максимальная длина описания фильма — {} символов", MAX_DESC_LENGTH);
            throw new ValidationException(String.format("Максимальная длина описания фильма — %d символов",
                    MAX_DESC_LENGTH));
        }

        if (LocalDate.parse(film.getReleaseDate()).isBefore(MIN_RELEASE_DATE)) {
            log.info("Дата релиза фильма не может быть раньше {}", MIN_RELEASE_DATE);
            throw new ValidationException(String.format("Дата релиза фильма не может быть раньше %s",
                    MIN_RELEASE_DATE));
        }

        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}
