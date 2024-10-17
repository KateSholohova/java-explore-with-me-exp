package ru.practicum.comments;

import ru.practicum.events.EventMapper;
import ru.practicum.users.UserMapper;

import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setId(comment.getId());
        commentDto.setCreated(comment.getCreated().format(formatter));
        commentDto.setCommentator(UserMapper.toUserShortDto(comment.getCommentator()));
        commentDto.setEvent(EventMapper.toEventShortDto(comment.getEvent()));
        return commentDto;
    }
}
