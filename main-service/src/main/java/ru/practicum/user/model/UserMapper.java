package ru.practicum.user.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(NewUserRequest newUserRequest) {
        return new UserDto(0, newUserRequest.getName(), newUserRequest.getEmail());
    }

    public UserShortDto toUserShortDto(UserDto userDto) {
        return new UserShortDto(userDto.getId(), userDto.getName());
    }
}
