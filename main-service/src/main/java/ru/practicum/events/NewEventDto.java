package ru.practicum.events;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.events.location.Location;

@Data
public class NewEventDto {
    @NotBlank
    private String annotation;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    @Future
    private String eventDate;
    @NotBlank
    private boolean paid;
    @NotBlank
    private boolean requestModeration;
    @NotBlank
    private int participantLimit;
    @NotBlank
    private int category;
    @NotBlank
    private Location location;

}
