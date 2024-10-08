package ru.practicum.compilations;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.events.Event;
import ru.practicum.events.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private int id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
