package ru.practicum.events;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByInitiatorId(int id);

    Event findEventByIdAndInitiatorId(int eventId, int initiatorId);

    boolean existsByIdAndInitiatorId(int eventId, int initiatorId);

    List<Event> findAllByInitiatorIdInAndCategoryIdIn(List<Integer> initiatorIds, List<Integer> categoryIds);

    List<Event> findAllByCategoryIdIn(List<Integer> categoryIds);

    List<Event> findAllByIdIn(List<Integer> ids);
}
