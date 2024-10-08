package ru.practicum.events;

import lombok.Data;
import ru.practicum.categories.CategoryDto;
import ru.practicum.events.location.Location;
import ru.practicum.users.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventFullDto {
    private int id;
    private String annotation;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private boolean paid;
    private boolean requestModeration;
    private int participantLimit;
    private State state;
    private CategoryDto category;
    private UserShortDto initiator;
    private Location location;
    private int confirmedRequests;
    private int views;

}
