package ru.practicum.events;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.events.location.Location;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private String title;
    private String description;
    @Future
    private LocalDateTime eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    private int participantLimit;
    private int category;
    private Location location;
    private  StateActionAdmin stateAction;
}
