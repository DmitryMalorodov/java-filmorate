package ru.yandex.practicum.filmorate.model.film;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MPA {
    private Integer id;
    private String name;
}
