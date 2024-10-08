package ru.practicum.compilations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
    private List<Integer> events;
}
