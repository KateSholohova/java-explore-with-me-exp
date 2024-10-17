package ru.practicum.comments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.Event;
import ru.practicum.users.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String text;
    @NotNull
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private User commentator;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
