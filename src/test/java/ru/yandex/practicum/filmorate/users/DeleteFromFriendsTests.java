package ru.yandex.practicum.filmorate.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.constant.message.UserValidationMessages.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.practicum.filmorate.users.UserData.user;
import static ru.yandex.practicum.filmorate.users.UserData.user2;

@DisplayName("Проверка удаления из списока друзей")
public class DeleteFromFriendsTests extends UserTest {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public DeleteFromFriendsTests(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        super(userRepository);
        this.friendshipRepository = friendshipRepository;
    }

    @Test
    void checkDeleteFromFriends() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //подтверждение добавления в друзья от friendId
        mockMvc.perform(put(USERS_ID_FRIENDS_FRIEND_ID, friendId, userId))
                .andExpect(status().isOk());

        //удаление из друзей
        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userId, friendId))
                .andExpect(status().isOk());

        //проверка что у юзера с userId пустой список друзей
        mockMvc.perform(get(USERS_ID_FRIENDS, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        //проверка что у юзера с friendId пустой список друзей
        mockMvc.perform(get(USERS_ID_FRIENDS, friendId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void checkDeleteFriendWithDoesNotExistId() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendNotExistId = 4L;

        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, friendNotExistId)));
    }

    @Test
    void checkDeleteFriendWithDoesNotExistId2() throws Exception {
        Long userNotExistId = 2L;
        Long friendNotExistId = getIdFromObject(createUser(user));

        mockMvc.perform(delete(USERS_ID_FRIENDS_FRIEND_ID, userNotExistId, friendNotExistId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userNotExistId)));
    }

    @Test
    void checkDeleteFromFriendsDB() throws Exception {
        Long userId = getIdFromObject(createUser(user));
        Long friendId = getIdFromObject(createUser(user2));

        //добавление в друзья от userId
        friendshipRepository.addFriend(userId, friendId);
        //подтверждение добавления в друзья от friendId
        friendshipRepository.deleteFriend(friendId, userId);

        //удаление из друзей
        friendshipRepository.deleteFriend(userId, friendId);

        //проверка что у юзера с userId пустой список друзей
        Assertions.assertTrue(userRepository.getFriendsList(userId).isEmpty());

        //проверка что у юзера с friendId пустой список друзей
        Assertions.assertTrue(userRepository.getFriendsList(friendId).isEmpty());
    }
}
