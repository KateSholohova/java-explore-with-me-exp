package ru.practicum.events;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.location.Location;

@Getter
@Setter
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotBlank
    private String eventDate;
    private Boolean paid;
    private Boolean requestModeration;
    @PositiveOrZero
    private int participantLimit;
    @NotNull
    private int category;
    @NotNull
    private Location location;

}
