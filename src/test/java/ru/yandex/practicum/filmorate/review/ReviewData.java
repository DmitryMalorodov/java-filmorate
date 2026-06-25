package ru.yandex.practicum.filmorate.review;

import ru.yandex.practicum.filmorate.model.review.Review;

public class ReviewData {

    public static final Review review = Review.builder()
            .content("Отличный фильм! Очень рекомендую.")
            .isPositive(true)
            .userId(1L)
            .filmId(1L)
            .build();

    static final Review review2 = Review.builder()
            .content("Не понравилось, слишком затянуто.")
            .isPositive(false)
            .userId(2L)
            .filmId(1L)
            .build();

    static final Review review3 = Review.builder()
            .content("Средненько, но посмотреть можно.")
            .isPositive(true)
            .userId(3L)
            .filmId(2L)
            .build();

    static final Review reviewWithHighUseful = Review.builder()
            .content("Фильм просто шедевр!")
            .isPositive(true)
            .userId(4L)
            .filmId(1L)
            .useful(10)
            .build();

    static final Review reviewWithNegativeUseful = Review.builder()
            .content("Очень разочарован этим фильмом.")
            .isPositive(false)
            .userId(5L)
            .filmId(1L)
            .useful(-5)
            .build();

    static final String LONG_CONTENT = "Очень длинный отзыв. ".repeat(20) + "Конец.";
    static final String TOO_LONG_CONTENT = "Очень длинный отзыв. ".repeat(100);

    static final long USER_ID_LIKE = 10L;
    static final long USER_ID_DISLIKE = 11L;
}