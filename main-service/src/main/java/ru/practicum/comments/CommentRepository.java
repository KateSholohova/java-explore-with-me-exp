package ru.practicum.comments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByCommentatorId(int id);

    List<Comment> findAllByEventIdIn(List<Integer> ids);

    List<Comment> findAllByEventId(int id);

    boolean existsByCommentatorIdAndEventId(int id, int eventId);
}
