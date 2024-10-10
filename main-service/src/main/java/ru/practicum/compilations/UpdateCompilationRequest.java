package ru.practicum.compilations;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    private List<Integer> events;
}
