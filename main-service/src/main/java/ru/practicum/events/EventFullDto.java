package ru.practicum.events;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.categories.CategoryDto;
import ru.practicum.events.location.Location;
import ru.practicum.users.UserShortDto;

@Getter
@Setter
public class EventFullDto {
    private int id;
    private String annotation;
    private String title;
    private String description;
    private String eventDate;
    private String createdOn;
    private String publishedOn;
    private boolean paid;
    private boolean requestModeration;
    private int participantLimit;
    private State state;
    private CategoryDto category;
    private UserShortDto initiator;
    private Location location;
    private int confirmedRequests;
    private long views;

}
