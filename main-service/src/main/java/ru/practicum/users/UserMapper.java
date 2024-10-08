package ru.practicum.users;

public class UserMapper {

    public static User fromUserShortDtoToUser(UserShortDto userShortDto) {
        User user = new User();
        user.setName(userShortDto.getName());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}
