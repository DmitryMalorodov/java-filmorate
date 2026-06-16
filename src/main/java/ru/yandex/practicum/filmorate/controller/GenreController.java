package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.constant.endpoint.GenreEndpoints;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping(GenreEndpoints.FILMS_GENRES)
    public Collection<GenreDto> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping(GenreEndpoints.FILMS_GENRES_ID)
    public GenreDto getGenreById(@PathVariable final Integer id) {
        return genreService.getGenreById(id);
    }
}
