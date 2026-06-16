package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.constant.endpoint.MpaEndpoints;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping(MpaEndpoints.FILMS_MPA)
    public Collection<MpaDto> getMpa() {
        return mpaService.getMpa();
    }

    @GetMapping(MpaEndpoints.FILMS_MPA_ID)
    public MpaDto getMpaById(@PathVariable final Integer id) {
        return mpaService.getMpaById(id);
    }
}
