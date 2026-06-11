package ru.yandex.practicum.filmorate.dal.old.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAll();

    Optional<Film> findFilmById(Long filmId);

    Film create(Film film);
}
