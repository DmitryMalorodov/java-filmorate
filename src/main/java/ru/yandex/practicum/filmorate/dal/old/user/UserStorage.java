package ru.yandex.practicum.filmorate.dal.old.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAll();

    Optional<User> findUserById(Long userId);

    User create(User user);
}
