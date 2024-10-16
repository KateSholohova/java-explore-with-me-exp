package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.Event;
import ru.practicum.events.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.requests.RequestRepository;
import ru.practicum.requests.Status;
import ru.practicum.users.User;
import ru.practicum.users.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public CommentDto create(NewCommentDto newCommentDto, int userId, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Event date is after now");
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            if (requestRepository.findByRequesterIdAndEventId(userId, eventId).getStatus().equals(Status.CONFIRMED)) {
                if (commentRepository.existsByCommentatorIdAndEventId(userId, eventId)) {
                    throw new ConflictException("Comment already exists");
                }
                Comment comment = CommentMapper.toComment(newCommentDto);
                comment.setCommentator(user);
                comment.setEvent(event);
                comment.setCreated(LocalDateTime.now());
                commentRepository.save(comment);
                return CommentMapper.toCommentDto(comment);
            }
            throw new ConflictException("Request is not confirmed yet");
        }
        throw new ConflictException("Request is not exist");
    }

    @Transactional
    public CommentDto update(NewCommentDto newCommentDto, int userId, int commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
        comment.setText(newCommentDto.getText());
        return CommentMapper.toCommentDto(comment);
    }

    @Transactional
    public void deleteByCommentator(int commentId, int userId) {
        if (commentRepository.existsById(commentId)) {
            if (commentRepository.findById(commentId).get().getCommentator().getId() == userId) {
                commentRepository.deleteById(commentId);
            } else {
                throw new ValidationException("It is not your comment");
            }

        }
        throw new NotFoundException("Comment not found");
    }

    @Transactional
    public List<CommentDto> getAllUserComments(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return commentRepository.findAllByCommentatorId(userId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @Transactional
    public List<CommentDto> getCommentsForUserEvents(int userId) {
        List<Integer> eventIds = eventRepository.findAllIdsByInitiatorId(userId);
        return commentRepository.findAllByEventIdIn(eventIds).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @Transactional
    public List<CommentDto> findAllEventComments(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return commentRepository.findAllByEventId(eventId).stream()
                .map(CommentMapper::toCommentDto)
                .toList();
    }

    @Transactional
    public CommentDto findById(int commentId) {
        if (commentRepository.existsById(commentId)) {
            return CommentMapper.toCommentDto(commentRepository.findById(commentId).get());
        }
        throw new NotFoundException("Comment not found");
    }

    @Transactional
    public void deleteByAdmin(int commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        }
        throw new NotFoundException("Comment not found");
    }
}


