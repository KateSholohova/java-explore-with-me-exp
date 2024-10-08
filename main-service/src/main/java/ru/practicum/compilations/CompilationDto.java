package ru.practicum.compilations;

import lombok.Data;
import ru.practicum.events.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private int id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
