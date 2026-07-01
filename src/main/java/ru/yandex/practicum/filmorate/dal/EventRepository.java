package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.OperationType;

import java.time.Instant;
import java.util.List;

@Repository
public class EventRepository extends BaseRepository<Event> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM user_events " +
            "WHERE user_id = ? " +
            "ORDER BY created_at ASC";
    private static final String ADD_EVENT_QUERY = "INSERT INTO user_events(user_id, entity_id, event_type, operation, created_at)" +
            "VALUES (?, ?, ?, ?, ?)";

    public EventRepository(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    public List<Event> findAllByUserId(Long userId) {
        return findMany(FIND_ALL_QUERY, userId);
    }

    public void save(Long userId, Long entityId, EventType eventType, OperationType operationType) {
        insert(
                ADD_EVENT_QUERY,
                userId,
                entityId,
                eventType.toString(),
                operationType.toString(),
                Instant.now().toEpochMilli()
        );
    }
}
