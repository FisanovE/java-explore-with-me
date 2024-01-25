package ru.practicum.ewm.admin_api.users;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.admin_api.users.model.NewUserRequest;
import ru.practicum.ewm.admin_api.users.model.User;
import ru.practicum.ewm.admin_api.users.model.UserDto;
import ru.practicum.ewm.admin_api.users.model.UserShortDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

}
