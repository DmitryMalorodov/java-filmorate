package ru.yandex.practicum.filmorate;

import java.util.Comparator;
import java.util.Set;

public class Helper {

    public static Long getNextId(Set<Long> set) {
        Long maxId = set.stream()
                .max(Comparator.naturalOrder())
                .orElse(0L);
        return ++maxId;
    }
}
