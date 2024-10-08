package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    boolean existsByRequesterIdAndEventId(int requesterId, int eventId);

    List<Request> findAllByRequesterId(int requesterId);

    List<Request> findAllByEventId(int eventId);

    List<Request> findAllByIdIn(List<Integer> ids);
}
