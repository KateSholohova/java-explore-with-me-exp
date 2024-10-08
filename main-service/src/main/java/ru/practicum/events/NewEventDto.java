package ru.practicum.events;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private boolean paid;
    @NotNull
    private boolean requestModeration;
    @NotNull
    private int participantLimit;
    @NotNull
    private int category;
    @NotNull
    private Location location;

}
