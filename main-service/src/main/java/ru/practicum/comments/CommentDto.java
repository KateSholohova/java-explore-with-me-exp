package ru.practicum.comments;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.EventShortDto;
import ru.practicum.users.UserShortDto;

@Getter
@Setter
public class CommentDto {
    private int id;
    private String text;
    private String created;
    private UserShortDto commentator;
    private EventShortDto event;
}
