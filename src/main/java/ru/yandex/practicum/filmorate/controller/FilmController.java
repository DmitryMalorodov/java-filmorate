package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.endpoint.FilmEndpoints;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.NEGATIVE_LIMIT_MESSAGE;

@RestController
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping(FilmEndpoints.FILMS_ID)
    public FilmDto getFilmByiD(@PathVariable final Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping(FilmEndpoints.FILMS)
    public Collection<FilmDto> findAll() {
        return filmService.getFilms();
    }

    @PostMapping(FilmEndpoints.FILMS)
    public FilmDto create(@Validated(OnCreate.class) @RequestBody final Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping(FilmEndpoints.FILMS)
    public FilmDto update(@Validated(OnUpdate.class) @RequestBody final Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping(FilmEndpoints.FILMS_ID_LIKE_USER_ID)
    public void addLikeToFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(FilmEndpoints.FILMS_ID_LIKE_USER_ID)
    public void deleteLikeFromFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping(FilmEndpoints.FILMS_POPULAR)
    public Collection<FilmDto> getPopularFilms(
            @Min(value = 0, message = NEGATIVE_LIMIT_MESSAGE) @RequestParam(defaultValue = "10") final Integer count) {
        return filmService.getMostPopularFilmsByLikes(count);
    }
}
