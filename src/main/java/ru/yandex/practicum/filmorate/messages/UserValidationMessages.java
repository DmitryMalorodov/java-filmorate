package ru.yandex.practicum.filmorate.messages;

public class UserValidationMessages extends ValidationMessages {
    public static final String EMAIL_BLANK_MESSAGE = "Электронная почта не может быть пустой";
    public static final String EMAIL_NOT_CORRECT_MESSAGE = "Электронная почта не соответствует требуемому формату";
    public static final String LOGIN_BLANK_MESSAGE = "Логин не может быть пустой";
    public static final String BIRTHDAY_COULD_NOT_BE_IN_FUTURE_MESSAGE = "Дата рождения не может быть в будущем";
}
