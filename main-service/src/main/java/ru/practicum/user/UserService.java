package ru.practicum.user;

import ru.practicum.user.model.NewUserRequest;
import ru.practicum.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(NewUserRequest newUser);

    void deleteUser(int userId);

    List<UserDto> findUsers(List<Integer> ids, int from, int size);
}
