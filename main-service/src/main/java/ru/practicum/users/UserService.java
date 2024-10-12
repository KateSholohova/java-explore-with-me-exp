package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {
        User user = UserMapper.fromNewUserRequestToUser(newUserRequest);
        userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public void delete(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException("Category not found");
        }
    }

    @Transactional
    public List<UserDto> findAll(int from, int size, List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllByIdIn(ids).stream()
                    .map(UserMapper::toUserDto)
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll().stream()
                    .map(UserMapper::toUserDto)
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
    }

}
