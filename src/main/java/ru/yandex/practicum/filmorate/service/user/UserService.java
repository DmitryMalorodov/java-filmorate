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

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

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
    }

    public void deleteFriend(Long userId, Long friendId) {
        //вызов методов поиска юзеров для проверки, что они существуют
        getUserById(userId);
        getUserById(friendId);
        log.info("Удаление из друзей пользователей с id - {}, {}", userId, friendId);
        friendshipRepository.deleteFriend(userId, friendId);
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

    private void setName(User oldUser, User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
    }
}
