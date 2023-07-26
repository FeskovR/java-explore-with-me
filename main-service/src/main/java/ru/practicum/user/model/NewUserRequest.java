package ru.practicum.user.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class NewUserRequest {
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @NotBlank
    @Length(min = 6, max = 254)
    @Email
    private String email;
}
