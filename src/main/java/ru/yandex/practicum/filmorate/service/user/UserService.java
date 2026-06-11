package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.dal.old.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserStorage userStorage;

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + userId));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(User user) {
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
        log.info("Добавление в друзья пользователей с id - {}, {}", userId, friendId);
        friendshipRepository.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей с id - {}, {}", userId, friendId);
        friendshipRepository.deleteFriend(userId, friendId);
    }

    public List<UserDto> getCommonFriendsList(Long userId, Long otherUserId) {
        log.info("Получение списка общих друзей пользователей с id - {}, {}", userId, otherUserId);
        return userRepository.getCommonFriendsList(userId, otherUserId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public List<UserDto> getUserFriendsList(Long userId) {
        log.info("Получение списка друзей пользователя с id - {}", userId);
        return userRepository.getFriendsList(userId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }









    public User findUserById(Long userId) {
        log.info("Поиск пользователя по id - {}", userId);
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));
    }

    private void setName(User oldUser, User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
    }
}
