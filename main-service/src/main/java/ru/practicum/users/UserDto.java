package ru.practicum.users;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    private int id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
