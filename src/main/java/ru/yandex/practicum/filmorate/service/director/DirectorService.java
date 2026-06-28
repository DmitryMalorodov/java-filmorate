package ru.yandex.practicum.filmorate.service.director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.director.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    //GET /directors
    public List<DirectorDto> findAll() {
        return directorRepository.findAll().stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    //GET /directors/{id}
    public DirectorDto findById(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Режиссёр с id = " + id + " не найден"));

        return DirectorMapper.mapToDirectorDto(director);
    }

    //POST /directors
    public DirectorDto create(Director director) {
        Director saved = directorRepository.save(director);
        return DirectorMapper.mapToDirectorDto(saved);
    }


    //PUT /directors
    public DirectorDto update(Director director) {
        findById(director.getId());
        Director updated = directorRepository.update(director);
        return DirectorMapper.mapToDirectorDto(updated);
    }

    //DELETE /directors/{id}
    public void delete(Long id) {
        findById(id);
        directorRepository.deleteById(id);
    }
}