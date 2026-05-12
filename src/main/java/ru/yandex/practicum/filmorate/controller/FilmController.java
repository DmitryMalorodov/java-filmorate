package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable final Long id) {
        return filmService.findFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Validated(OnCreate.class) @RequestBody final Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody final Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.deleteLikeFromFilm(userId, id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam final Integer count) {
        return filmService.getMostPopularFilmsByLikes(count);
    }
}
