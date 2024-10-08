package ru.practicum.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserShortDto {
    Long id;
    @NotBlank
    String name;
}
