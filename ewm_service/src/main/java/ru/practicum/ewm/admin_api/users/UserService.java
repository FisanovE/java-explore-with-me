package ru.practicum.ewm.admin_api.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin_api.users.model.NewUserRequest;
import ru.practicum.ewm.admin_api.users.model.User;
import ru.practicum.ewm.admin_api.users.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto create(NewUserRequest newUserRequest) {
        User user = userMapper.toUser(newUserRequest);
        User addedUser = userRepository.save(user);
        return userMapper.toUserDto(addedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        if (ids == null) {
            return userRepository.findAll(PageRequest.of(from / size, size)).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids, PageRequest.of(from / size, size)).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }
}
