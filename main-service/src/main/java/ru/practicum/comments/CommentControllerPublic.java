package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentControllerPublic {

    private final CommentService commentService;

    @GetMapping("/event/{eventId}")
    public List<CommentDto> findAllEventComments(@PathVariable int eventId) {
        return commentService.findAllEventComments(eventId);
    }

    @GetMapping("/{commentId}")
    public CommentDto findById(@PathVariable int commentId) {
        return commentService.findById(commentId);
    }
}
