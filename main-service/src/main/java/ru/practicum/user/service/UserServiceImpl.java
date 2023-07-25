package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.user.model.NewUserRequest;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public User addUser(NewUserRequest newUser) {
        User user = UserMapper.toUserDto(newUser);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id: " + userId + " not found")
        );

        userRepository.deleteById(user.getId());
    }

    @Override
    public List<User> findUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<User> usersPage;
        if (ids != null && ids.size() > 0) {
            usersPage = userRepository.getUsersByParams(ids, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }
        return usersPage.toList();
    }
}
