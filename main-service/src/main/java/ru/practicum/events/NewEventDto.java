package ru.practicum.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    private String eventDate;
    @NotNull
    private boolean paid;
    @NotNull
    private boolean requestModeration;
    @NotNull
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private int category;
    @NotNull
    private Location location;

}
