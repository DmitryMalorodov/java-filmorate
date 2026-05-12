package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.Helper.getNextId;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(getNextId(users.keySet()));
        setName(user, user);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        User oldUser = users.get(newUser.getId());
        log.info("Пользователь для редактирования {}", oldUser);
        if (oldUser != null) {
            if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) oldUser.setEmail(newUser.getEmail());
            if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            setName(oldUser, newUser);
            log.info("Отредактированный пользователь {}", oldUser);
            return oldUser;
        }

        log.info("Пользователь с id {} отсутствует", newUser.getId());
        throw new ValidationException(String.format("Пользователь с id '%d' отсутствует", newUser.getId()));
    }

    private void setName(User oldUser, User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
    }
}
