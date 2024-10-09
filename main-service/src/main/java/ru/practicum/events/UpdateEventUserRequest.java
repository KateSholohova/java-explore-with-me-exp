package ru.practicum.events;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ru.practicum.events.location.Location;

import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private String title;
    private String description;
    @Future
    private LocalDateTime eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    @PositiveOrZero
    private int participantLimit;
    private int category;
    private Location location;
    private StateActionUser stateAction;

}
