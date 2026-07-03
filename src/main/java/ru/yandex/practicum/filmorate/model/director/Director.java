package ru.yandex.practicum.filmorate.model.director;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Director {
    private Long id;

    @NotBlank(message = "Имя режиссёра не может быть пустым")
    private String name;
}
