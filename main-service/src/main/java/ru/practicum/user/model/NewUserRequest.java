package ru.practicum.user.model;

import lombok.Data;

@Data
public class NewUserRequest {
    private String name;
    private String email;
}
