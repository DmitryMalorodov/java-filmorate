package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Helper.getNextId;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Validated(OnCreate.class) @RequestBody Film film) {
        film.setId(getNextId(films.keySet()));
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        log.info("Фильм для редактирования {}", oldFilm);
        if (oldFilm != null) {
            if (newFilm.getName() != null && !newFilm.getName().isBlank()) oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Отредактированный фильм {}", oldFilm);
            return oldFilm;
        }

        log.info("Фильм с id {} отсутствует", newFilm.getId());
        throw new ValidationException(String.format("Фильм с id '%d' отсутствует", newFilm.getId()));
    }

    public void cleanFilms() {
        films.clear();
    }
}
