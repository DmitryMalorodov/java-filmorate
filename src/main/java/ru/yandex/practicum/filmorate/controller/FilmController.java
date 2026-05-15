package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping(FilmEndpoints.FILMS_ID)
    public Film findFilmById(@PathVariable final Long id) {
        return filmService.findFilmById(id);
    }

    @GetMapping(FilmEndpoints.FILMS)
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping(FilmEndpoints.FILMS)
    public Film create(@Validated(OnCreate.class) @RequestBody final Film film) {
        return filmService.create(film);
    }

    @PutMapping(FilmEndpoints.FILMS)
    public Film update(@Validated(OnUpdate.class) @RequestBody final Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping(FilmEndpoints.FILMS_ID_LIKE_USER_ID)
    public void addLikeToFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping(FilmEndpoints.FILMS_ID_LIKE_USER_ID)
    public void deleteLikeFromFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.deleteLikeFromFilm(userId, id);
    }

    @GetMapping(FilmEndpoints.FILMS_POPULAR)
    public Collection<Film> getPopularFilms(@RequestParam final Integer count) {
        return filmService.getMostPopularFilmsByLikes(count);
    }
}
