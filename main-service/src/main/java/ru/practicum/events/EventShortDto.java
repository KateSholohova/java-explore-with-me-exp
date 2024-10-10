package ru.practicum.events;

import lombok.Data;
import ru.practicum.categories.CategoryDto;
import ru.practicum.users.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private int id;
    private String annotation;
    private String title;
    private LocalDateTime eventDate;
    private boolean paid;
    private CategoryDto category;
    private UserShortDto initiator;
    private int confirmedRequests;
    private long views;
}
