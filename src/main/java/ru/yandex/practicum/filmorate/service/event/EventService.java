package ru.yandex.practicum.filmorate.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.EventRepository;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public Collection<EventDto> getEventsByUserId(Long userId) {
        return eventRepository.findAllByUserId(userId).stream()
                .map(EventMapper::mapToEventDto)
                .toList();
    }

    public void addEvent(Long userId, Long entityId, EventType eventType, OperationType operationType) {
        eventRepository.save(userId, entityId, eventType, operationType);
    }
}
