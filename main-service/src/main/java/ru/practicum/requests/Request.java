package ru.practicum.requests;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.Event;
import ru.practicum.users.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime created;
    private Status status;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
