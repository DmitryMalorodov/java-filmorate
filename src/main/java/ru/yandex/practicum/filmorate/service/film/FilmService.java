package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.director.DirectorService;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final GenreService genreService;
    private final MpaService mpaService;
    private final DirectorService directorService;
    private final EventService eventService;

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
        //проверка, что переданные id жанров и режиссеров существуют в БД
        checkGenresExist(film);
        checkDirectorsExist(film);

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
        if (newFilm.getDirectors() != null && !newFilm.getDirectors().isEmpty())
            oldFilm.setDirectors(newFilm.getDirectors());
        if (newFilm.getDirectors() != null && !newFilm.getDirectors().isEmpty()) oldFilm.setDirectors(newFilm.getDirectors());

        filmRepository.update(oldFilm);
        log.info("Отредактированный фильм {}", oldFilm);
        return FilmMapper.mapToFilmDto(oldFilm);
    }

    public void addLike(Long filmId, Long userId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
        log.info("Добавление лайка пользователем с id - {} к фильму с id - {}", userId, filmId);
        filmRepository.addLike(filmId, userId);
        eventService.addEvent(userId, filmId, EventType.LIKE, OperationType.ADD);
    }

    public void deleteLike(Long filmId, Long userId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
        log.info("Удаление лайка пользователем с id - {} с фильма с id - {}", userId, filmId);
        filmRepository.deleteLike(filmId, userId);
        eventService.addEvent(userId, filmId, EventType.LIKE, OperationType.REMOVE);
    }

    public Collection<FilmDto> getMostPopularFilms(Integer limit, Integer genreId, Integer year) {
        log.info("Получение списка самых популярных фильмов по лайкам с ограничением по кол-ву фильмов - {}", limit);
        return filmRepository.getPopularFilms(limit, genreId, year)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public Collection<FilmDto> getFilmByRequestParam(String query, List<String> searchType) {
        log.info("Поиск фильма по фразе '{}' ", query);
        if (query.isBlank()) return getFilms();

        return filmRepository.getFilmsByRequestParam(query, searchType)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public void deleteFilm(Long filmId) {
        //вызов метода получения фильма по id для проверки его существования
        getFilmById(filmId);
        filmRepository.deleteFilm(filmId);
    }

    public Collection<FilmDto> getCommonFilms(Long userId, Long friendId) {
        return filmRepository.getCommonFilms(userId, friendId)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public List<FilmDto> getFilmsByDirector(Long directorId, String sortBy) {
        List<Film> films = sortBy.equalsIgnoreCase("year") ? filmRepository.getFilmsByDirectorSortedByYear(directorId) : filmRepository.getFilmsByDirectorSortedByLikes(directorId);

        return films.stream().map(FilmMapper::mapToFilmDto).toList();
    }

    private void checkGenresExist(Film film) {
        boolean isGenresExist = film.getGenres().stream()
                .map(Genre::getId)
                .allMatch(genreId -> genreService.getGenres().stream()
                        .anyMatch(genreDB -> genreDB.getId().equals(genreId)));
        if (!isGenresExist) throw new NotFoundException("Переданные жанры не найдены");
    }

    private void checkDirectorsExist(Film film) {
        boolean isDirectorsExist = film.getDirectors().stream()
                .map(Director::getId)
                .allMatch(directorId -> directorService.findAll().stream()
                        .anyMatch(directorDB -> directorDB.getId().equals(directorId)));
        if (!isDirectorsExist) throw new NotFoundException("Переданные режиссёры не найдены");
    }
}
