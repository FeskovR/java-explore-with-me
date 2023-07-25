package ru.practicum.user.service;

import ru.practicum.user.model.NewUserRequest;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(NewUserRequest newUser);

    void deleteUser(int userId);

    List<User> findUsers(List<Integer> ids, int from, int size);
}
