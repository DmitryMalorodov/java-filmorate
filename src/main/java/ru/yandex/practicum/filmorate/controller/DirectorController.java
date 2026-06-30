package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<DirectorDto> getDirectors() {
        return directorService.findAll();
    }

    @GetMapping("/{id}")
    public DirectorDto getDirectorById(@PathVariable Long id) {
        return directorService.findById(id);
    }

    @PostMapping
    public DirectorDto create(@Valid @RequestBody Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public DirectorDto update(@Valid @RequestBody Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }
}
