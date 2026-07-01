package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.event.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .eventId(event.getEventId())
                .userId(event.getUserId())
                .entityId(event.getEntityId())
                .eventType(event.getEventType())
                .operation(event.getOperation())
                .timestamp(event.getTimestamp())
                .build();
    }
}
