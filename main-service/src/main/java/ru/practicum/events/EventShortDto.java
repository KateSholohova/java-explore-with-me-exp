package ru.practicum.events;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.categories.CategoryDto;
import ru.practicum.events.location.Location;
import ru.practicum.users.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private int id;
    @NotBlank
    private String annotation;
    @NotBlank
    private String title;
    @NotBlank
    @Future
    private LocalDateTime eventDate;
    @NotBlank
    private boolean paid;
    private CategoryDto category;
    private UserShortDto initiator;
    private int confirmedRequests;
    private int views;
}
