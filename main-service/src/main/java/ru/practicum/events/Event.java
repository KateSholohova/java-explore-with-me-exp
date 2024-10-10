package ru.practicum.events;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @Future
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @NotNull
    private Boolean paid;
    @NotNull
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @PositiveOrZero
    @NotNull
    @Column(name = "participant_limit")
    private int participantLimit;
    @Transient
    private long views;
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
