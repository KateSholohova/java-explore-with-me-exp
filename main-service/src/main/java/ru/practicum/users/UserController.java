package ru.practicum.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto create(@RequestBody @Valid NewUserRequest user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") int userId) {
        userService.delete(userId);
    }

    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false) List<Integer> ids,
                                 @RequestParam(defaultValue = "0")
                                 @PositiveOrZero Integer from,
                                 @RequestParam(defaultValue = "10")
                                 @Positive Integer size) {
        return userService.findAll(from, size, ids);
    }

}
