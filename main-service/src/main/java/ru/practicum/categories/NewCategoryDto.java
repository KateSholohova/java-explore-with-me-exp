package ru.practicum.categories;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class NewCategoryDto {
    @NotBlank
    @Length(min = 1, max = 50)
    @Column(unique = true)
    private String name;
}
