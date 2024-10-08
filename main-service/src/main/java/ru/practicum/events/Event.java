package ru.practicum.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.categories.Category;
import ru.practicum.events.location.Location;
import ru.practicum.users.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String annotation;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    @Future
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private int confirmedRequests;
    @NotNull
    private Boolean paid;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    private int participantLimit;
    private int views;
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private State state;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

}
