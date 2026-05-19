package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;

import static ru.yandex.practicum.filmorate.constant.message.FilmValidationMessages.FILM_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film findFilmById(Long filmId) {
        log.info("Поиск фильма по id - {}", filmId);
        return filmStorage.findFilmById(filmId)
                .orElseThrow(() -> new NotFoundException(String.format(FILM_NOT_FOUND_MESSAGE, filmId)));
    }

    public Collection<Film> findAll() {
        log.info("Получение списка всех фильмов");
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        Film oldFilm = findFilmById(newFilm.getId());
        log.info("Фильм для редактирования {}", oldFilm);

        if (newFilm.getName() != null && !newFilm.getName().isBlank()) oldFilm.setName(newFilm.getName());
        if (newFilm.getDescription() != null) oldFilm.setDescription(newFilm.getDescription());
        if (newFilm.getReleaseDate() != null) oldFilm.setReleaseDate(newFilm.getReleaseDate());
        if (newFilm.getDuration() != null) oldFilm.setDuration(newFilm.getDuration());
        log.info("Отредактированный фильм {}", oldFilm);
        return oldFilm;

    }

    public void addLikeToFilm(Long userId, Long filmId) {
        User user = userService.findUserById(userId);
        Film film = findFilmById(filmId);

        log.info("Добавление лайка пользователем с id - {} к фильму с id - {}", userId, filmId);
        film.getLikesUsersId().add(user.getId());
    }

    public void deleteLikeFromFilm(Long userId, Long filmId) {
        User user = userService.findUserById(userId);
        Film film = findFilmById(filmId);

        log.info("Удаление лайка пользователем с id - {} с фильма с id - {}", userId, filmId);
        film.getLikesUsersId().remove(user.getId());
    }

    public Collection<Film> getMostPopularFilmsByLikes(int limit) {

        log.info("Получение списка самых популярных фильмов по лайкам с ограничением по кол-ву фильмов - {}", limit);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikesUsersId().size()).reversed())
                .limit(limit)
                .toList();
    }
}
