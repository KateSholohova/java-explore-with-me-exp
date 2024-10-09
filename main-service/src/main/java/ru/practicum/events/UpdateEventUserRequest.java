package ru.practicum.events;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.events.location.Location;

@Data
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 3, max = 120)
    private String title;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    @PositiveOrZero
    private int participantLimit;
    private int category;
    private Location location;
    private StateActionUser stateAction;

}
