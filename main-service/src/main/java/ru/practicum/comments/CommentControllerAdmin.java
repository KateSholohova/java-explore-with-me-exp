package ru.practicum.comments;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteByAdmin(@PathVariable("commentId") int commentId) {
        commentService.deleteByAdmin(commentId);
    }
}
