package ru.practicum.comments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentControllerPrivate {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentDto create(@Valid @RequestBody NewCommentDto newCommentDto,
                             @PathVariable int userId,
                             @RequestParam int eventId) {
        return commentService.create(newCommentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable int userId,
                             @PathVariable int commentId,
                             @RequestBody NewCommentDto newCommentDto) {
        return commentService.update(newCommentDto, userId, commentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable int commentId, @PathVariable int userId) {
        commentService.deleteByCommentator(commentId, userId);
    }

    @GetMapping
    public List<CommentDto> getAllUserComments(@PathVariable int userId) {
        return commentService.getAllUserComments(userId);
    }

}
