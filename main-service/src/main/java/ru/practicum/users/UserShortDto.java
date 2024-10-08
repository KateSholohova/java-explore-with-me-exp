package ru.practicum.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserShortDto {
    Long id;
    @NotBlank
    String name;
}
