package ru.practicum.compilations;

import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    private Boolean pinned;
    private String title;
    private List<Integer> events;
}
