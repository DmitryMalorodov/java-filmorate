package ru.yandex.practicum.filmorate;

import org.assertj.core.api.SoftAssertions;

public class GeneralAssertions {

    public static <T> void isEqual(T actValue, T expValue, String errorMessage, SoftAssertions softAssert) {
        softAssert.assertThat(actValue)
                .as(String.format(errorMessage, actValue, expValue))
                .isEqualTo(expValue);
    }
}
