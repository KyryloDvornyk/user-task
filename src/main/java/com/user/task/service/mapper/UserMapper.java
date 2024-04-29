package com.user.task.service.mapper;

import com.user.task.dto.request.UserRequestDto;
import com.user.task.dto.response.UserResponseDto;
import com.user.task.model.User;
import org.springframework.stereotype.Component;

import static java.util.Objects.*;

@Component
public class UserMapper {
    private final LocalAddressMapper localAddressMapper;

    UserMapper(LocalAddressMapper localAddressMapper) {
        this.localAddressMapper = localAddressMapper;
    }

    public UserResponseDto mapToDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthDate(user.getBirthDate());
        if (nonNull(user.getAddress())) {
            userDto.setAddress(localAddressMapper.mapToDto(user.getAddress()));
        }
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    public User mapToEntity(UserRequestDto requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setBirthDate(requestDto.getBirthDate());
        if (nonNull(requestDto.getAddress())) {
            user.setAddress(localAddressMapper.mapToEntity(requestDto.getAddress()));
        }
        user.setPhoneNumber(requestDto.getPhoneNumber());
        return user;
    }
}
