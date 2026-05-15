package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User findUserById(Long userId) {
        log.info("Поиск пользователя по id - {}", userId);
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    public Collection<User> findAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.findAll();
    }

    public User create(User user) {
        setName(user, user);
        return userStorage.create(user);
    }

    public User update(User newUser) {
        User oldUser = findUserById(newUser.getId());
        log.info("Пользователь для редактирования {}", oldUser);

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) oldUser.setEmail(newUser.getEmail());
        if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        setName(oldUser, newUser);
        log.info("Отредактированный пользователь {}", oldUser);
        return oldUser;
    }

    public void addUserToFriends(Long userId, Long friendId) {
        User friend = findUserById(friendId);
        User user = findUserById(userId);

        log.info("Добавление в друзья пользователей с id - {}, {}", userId, friendId);
        friend.getFriendsIds().add(user.getId());
        user.getFriendsIds().add(friend.getId());
    }

    public void deleteUserFromFriends(Long userId, Long friendId) {
        User friend = findUserById(friendId);
        User user = findUserById(userId);

        log.info("Удаление из друзей пользователей с id - {}, {}", userId, friendId);
        user.getFriendsIds().remove(friend.getId());
        friend.getFriendsIds().remove(user.getId());
    }

    public Collection<User> getUserFriendsList(Long userId) {
        Set<Long> userFriendsIds = findUserById(userId).getFriendsIds();
        log.info("Получение списка друзей пользователя с id - {}", userId);
        return userFriendsIds.stream()
                .map(this::findUserById)
                .toList();
    }

    public Collection<User> getCommonFriendsList(Long userId, Long otherUserId) {
        Set<Long> friendsIdsOfUser = findUserById(userId).getFriendsIds();
        Set<Long> friendsIdsOfOtherUser = findUserById(otherUserId).getFriendsIds();

        log.info("Получение списка общих друзей пользователей с id - {}, {}", userId, otherUserId);
        return friendsIdsOfUser.stream()
                .filter(friendsIdsOfOtherUser::contains)
                .map(this::findUserById)
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
