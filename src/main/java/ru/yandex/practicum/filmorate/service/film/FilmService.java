package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreService genreService;
    private final MpaService mpaService;

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
        //проверка, что переданные id жанров существуют в БД
        boolean isGenresExist = film.getGenres().stream()
                .map(Genre::getId)
                .allMatch(genreId -> genreService.getGenres().stream()
                        .anyMatch(genreDB -> genreDB.getId().equals(genreId)));
        if (!isGenresExist) throw new NotFoundException("Переданные жанры не найдены");

        //проверка, что переданный id mpa существует в БД
        mpaService.getMpaById(film.getMpa().getId());

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
        if (newFilm.getMpa() != null) oldFilm.setMpa(newFilm.getMpa());
        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) oldFilm.setGenres(newFilm.getGenres());
        filmRepository.update(oldFilm);
        log.info("Отредактированный фильм {}", oldFilm);
        return FilmMapper.mapToFilmDto(oldFilm);
    }

    public void addLike(Long filmId, Long userId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
        log.info("Добавление лайка пользователем с id - {} к фильму с id - {}", userId, filmId);
        filmRepository.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
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

    public Collection<FilmDto> getFilmByRequestParam(String query, List<String> searchType) {
        if (query.isEmpty()) getFilms();

        String userQuery = "%" + query.toLowerCase() + "%";

        StringBuilder sql = new StringBuilder("SELECT f.* FROM films f ");

        StringBuilder whereQuery = new StringBuilder("WHERE LOWER(f.name) LIKE ? ");

        List<Object> params = new ArrayList<>();
        params.add(userQuery);

        if (searchType.contains("directors")) {
            sql.append("INNER JOIN film_directors fd ON fd.film_id = f.id ");
            sql.append("INNER JOIN directors d ON d.id = fd.director_id ");

            whereQuery.append("OR LOWER(d.name) LIKE ? ");
            params.add(userQuery);
        }

        sql.append(whereQuery);
        return filmRepository.getFilmsByRequestParam(sql.toString(), params.toArray())
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public void deleteFilm(Long filmId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
        filmRepository.deleteFilm(filmId);
    }
}
