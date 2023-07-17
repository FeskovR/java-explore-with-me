package ru.practicum.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.user.model.NewUserRequest;
import ru.practicum.user.model.UserDto;
import ru.practicum.user.model.UserMapper;
import ru.practicum.util.Validator;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public UserDto addUser(NewUserRequest newUser) {
        Validator.validate(newUser);
        UserDto userDto = UserMapper.toUserDto(newUser);
        return userRepository.save(userDto);
    }

    @Override
    public void deleteUser(int userId) {
        UserDto user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id: " + userId + " not found")
        );

        userRepository.deleteById(user.getId());
    }

    @Override
    public List<UserDto> findUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<UserDto> usersPage;
        if (ids != null && ids.size() > 0) {
            usersPage = userRepository.getUsersByParams(ids, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }
        return usersPage.toList();
    }
}
