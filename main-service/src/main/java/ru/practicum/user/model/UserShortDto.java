package ru.practicum.user.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserShortDto {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
}
