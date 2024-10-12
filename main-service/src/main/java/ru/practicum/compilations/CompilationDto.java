package ru.practicum.compilations;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.events.EventShortDto;

import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private int id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
