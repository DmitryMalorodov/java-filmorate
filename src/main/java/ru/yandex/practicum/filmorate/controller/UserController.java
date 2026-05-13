package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.endpoint.UserEndpoints;
import ru.yandex.practicum.filmorate.marker.OnCreate;
import ru.yandex.practicum.filmorate.marker.OnUpdate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@RestController
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(UserEndpoints.USERS_ID)
    public User findUserById(@PathVariable final Long id) {
        return userService.findUserById(id);
    }

    @GetMapping(UserEndpoints.USERS)
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping(UserEndpoints.USERS)
    public User create(@Validated(OnCreate.class) @RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping(UserEndpoints.USERS)
    public User update(@Validated(OnUpdate.class) @RequestBody final User newUser) {
        return userService.update(newUser);
    }

    @GetMapping(UserEndpoints.USERS_ID_FRIENDS_COMMON_OTHER_ID)
    public Collection<User> getCommonFriendsList(@PathVariable final Long id, @PathVariable final Long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @GetMapping(UserEndpoints.USERS_ID_FRIENDS)
    public Collection<User> getUserFriendsList(@PathVariable final Long id) {
        return userService.getUserFriendsList(id);
    }

    @PutMapping(UserEndpoints.USERS_ID_FRIENDS_FRIEND_ID)
    public void addToFriends(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.addUserToFriends(id, friendId);
    }

    @DeleteMapping(UserEndpoints.USERS_ID_FRIENDS_FRIEND_ID)
    public void deleteFromFriends(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.deleteUserFromFriends(id, friendId);
    }
}
