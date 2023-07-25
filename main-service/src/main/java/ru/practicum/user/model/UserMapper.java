package ru.practicum.user.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User toUserDto(NewUserRequest newUserRequest) {
        return new User(0, newUserRequest.getName(), newUserRequest.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
