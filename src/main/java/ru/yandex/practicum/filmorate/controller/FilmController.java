package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.SortBy;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.NEGATIVE_LIMIT_MESSAGE;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable final Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Collection<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public FilmDto create(@Validated(OnCreate.class) @RequestBody final Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public FilmDto update(@Validated(OnUpdate.class) @RequestBody final Film newFilm) {
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getPopularFilms(
            @Positive(message = NEGATIVE_LIMIT_MESSAGE) @RequestParam(defaultValue = "10") final Integer count,
            @RequestParam(required = false) final Integer genreId,
            @RequestParam(required = false) final Integer year) {
        return filmService.getMostPopularFilms(count, genreId, year);
    }

    @GetMapping("/search")
    public Collection<FilmDto> findFilms(@RequestParam final String query,
                                         @RequestParam(name = "by") final List<String> params
    ) {
        return filmService.getFilmByRequestParam(query, params);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable final Long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public Collection<FilmDto> getCommonFilms(
            @RequestParam Long userId,
            @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public Collection<FilmDto> getFilmsByDirector(@PathVariable Long directorId,
                                                  @RequestParam(defaultValue = "likes") SortBy sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }
}
