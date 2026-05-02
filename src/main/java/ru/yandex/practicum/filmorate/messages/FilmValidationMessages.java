package ru.yandex.practicum.filmorate.messages;

public class FilmValidationMessages extends ValidationMessages {
    public static final String NAME_BLANK_MESSAGE = "Название фильма не может быть пустым";
    public static final String DESCRIPTION_MAX_LENGTH_MESSAGE = "Максимальная длина описания фильма — 200 символов";
    public static final String RELEASE_DATE_MIN_MESSAGE = "Дата релиза фильма не может быть раньше 1895-12-28";
    public static final String RELEASE_DATE_MIN = "1895-12-28";
    public static final String DURATION_MUST_BE_POSITIVE_MESSAGE = "Продолжительность фильма должна быть положительным числом";
}
