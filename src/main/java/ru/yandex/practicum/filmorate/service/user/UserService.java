package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findUserById(Long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    public Collection<User> findAll() {
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

    public void addUserToFriends(Long idOfUserWhoAdd, Long idOfUserToAdd) {
        User userToAdd = findUserById(idOfUserToAdd);
        User userWhoAdd = findUserById(idOfUserWhoAdd);

        userWhoAdd.getFriendsIds().add(userToAdd.getId());
        userToAdd.getFriendsIds().add(userWhoAdd.getId());
    }

    public void deleteUserFromFriends(Long idOfUserWhoDelete, Long idOfUserForDelete) {
        User userToAdd = findUserById(idOfUserForDelete);
        User userWhoAdd = findUserById(idOfUserWhoDelete);

        userWhoAdd.getFriendsIds().remove(userToAdd.getId());
        userToAdd.getFriendsIds().remove(userWhoAdd.getId());
    }

    public Collection<User> getCommonFriendsList(Long idOfUser1, Long idOfUser2) {
        Set<Long> friendsIdsOfUser1 = findUserById(idOfUser1).getFriendsIds();
        Set<Long> friendsIdsOfUser2 = findUserById(idOfUser2).getFriendsIds();

        return friendsIdsOfUser1.stream()
                .filter(friendsIdsOfUser2::contains)
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
