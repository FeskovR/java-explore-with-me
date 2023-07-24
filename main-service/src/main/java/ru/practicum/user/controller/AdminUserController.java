package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.service.UserService;
import ru.practicum.user.model.NewUserRequest;
import ru.practicum.user.model.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@AllArgsConstructor
public class AdminUserController {
    UserService userService;

    @GetMapping
    public List<UserDto> findUsers(@RequestParam(required = false) List<Integer> ids,
                                   @RequestParam(required = false, defaultValue = "0") int from,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Find users with params: ids {}, from {}, size {}", ids, from, size);
        return userService.findUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUser) {
        log.info("Adding new user name: {}, email: {}", newUser.getName(), newUser.getEmail());
        return userService.addUser(newUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        log.info("Deleting user id {}", userId);
        userService.deleteUser(userId);
    }
}
