package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.event.EventService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FilmRepository filmRepository;
    private final EventService eventService;

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
    }

    public Collection<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto createUser(User user) {
        log.info("Создание пользователя {}", user);
        setName(user, user);
        user = userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(User newUser) {
        User oldUser = UserMapper.mapToUser(getUserById(newUser.getId()));
        log.info("Пользователь для редактирования {}", oldUser);

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) oldUser.setEmail(newUser.getEmail());
        if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        setName(oldUser, newUser);
        userRepository.update(oldUser);
        log.info("Отредактированный пользователь {}", oldUser);
        return UserMapper.mapToUserDto(oldUser);
    }

    public void addFriend(Long userId, Long friendId) {
        //вызов методов поиска юзеров для проверки, что они существуют
        getUserById(userId);
        getUserById(friendId);
        log.info("Добавление в друзья пользователей с id - {}, {}", userId, friendId);
        friendshipRepository.addFriend(userId, friendId);
        eventService.addEvent(userId, friendId, EventType.FRIEND, OperationType.ADD);
    }

    public void deleteFriend(Long userId, Long friendId) {
        //вызов методов поиска юзеров для проверки, что они существуют
        getUserById(userId);
        getUserById(friendId);
        log.info("Удаление из друзей пользователей с id - {}, {}", userId, friendId);
        friendshipRepository.deleteFriend(userId, friendId);
        eventService.addEvent(userId, friendId, EventType.FRIEND, OperationType.REMOVE);
    }

    public Collection<UserDto> getCommonFriendsList(Long userId, Long otherUserId) {
        log.info("Получение списка общих друзей пользователей с id - {}, {}", userId, otherUserId);
        return userRepository.getCommonFriendsList(userId, otherUserId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public Collection<UserDto> getUserFriendsList(Long userId) {
        //вызов метода поиска юзера для проверки, что он существует
        getUserById(userId);
        log.info("Получение списка друзей пользователя с id - {}", userId);
        return userRepository.getFriendsList(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public void deleteUser(Long userId) {
        //вызов метода поиска юзера для проверки, что он существует
        getUserById(userId);
        userRepository.deleteUser(userId);
    }

    private void setName(User oldUser, User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
    }

    public List<FilmDto> getRecommendations(Long idUser) {
        log.info("Начал поиск рекомедаций");
        Set<Long> userLikeList = userRepository.getIdFilmsByUserId(idUser);
        Map<Long, Set<Long>> mindedUsers = userRepository.getMindedUsers(idUser);
        List<Long> recommendedFilms;
        log.info("Получен список лайкнутых фильмов пользователем {}", userLikeList);
        log.info("Получен список пользователей единомышлиников {}", mindedUsers);
        recommendedFilms = mindedUsers.values().stream()
                .flatMap(Set::stream)
                .filter(filmId -> !userLikeList.contains(filmId))
                .collect(Collectors.groupingBy(filmId -> filmId, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
        log.info("Нашел рекомендации {}", recommendedFilms);
        return filmRepository.getRecommendationsFilmsById(recommendedFilms)
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
