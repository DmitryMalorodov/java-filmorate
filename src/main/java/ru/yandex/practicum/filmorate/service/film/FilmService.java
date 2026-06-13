package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmDto getFilmById(Long filmId) {
        return filmRepository.findById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с id: " + filmId));
    }

    public Collection<FilmDto> getFilms() {
        return filmRepository.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto createFilm(Film film) {
        log.info("Создание фильма {}", film);
        film = filmRepository.save(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto update(Film newFilm) {
        Film oldFilm = FilmMapper.mapToFilm(getFilmById(newFilm.getId()));
        log.info("Фильм для редактирования {}", oldFilm);

        if (newFilm.getName() != null && !newFilm.getName().isBlank()) oldFilm.setName(newFilm.getName());
        if (newFilm.getDescription() != null) oldFilm.setDescription(newFilm.getDescription());
        if (newFilm.getReleaseDate() != null) oldFilm.setReleaseDate(newFilm.getReleaseDate());
        if (newFilm.getDuration() != null) oldFilm.setDuration(newFilm.getDuration());
        if (newFilm.getMpaId() != null) oldFilm.setMpaId(newFilm.getMpaId());
        filmRepository.update(oldFilm);
        log.info("Отредактированный фильм {}", oldFilm);
        return FilmMapper.mapToFilmDto(oldFilm);
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Добавление лайка пользователем с id - {} к фильму с id - {}", userId, filmId);
        filmRepository.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmRepository.deleteLike(filmId, userId);
        log.info("Удаление лайка пользователем с id - {} с фильма с id - {}", userId, filmId);
    }

    public Collection<FilmDto> getMostPopularFilmsByLikes(int limit) {
        log.info("Получение списка самых популярных фильмов по лайкам с ограничением по кол-ву фильмов - {}", limit);
        return filmRepository.getPopularFilms(limit)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
