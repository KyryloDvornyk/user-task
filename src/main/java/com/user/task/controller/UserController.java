package com.user.task.controller;

import com.user.task.dto.request.UserRequestDto;
import com.user.task.dto.response.UserResponseDto;
import com.user.task.exception.DataProcessingException;
import com.user.task.exception.ErrorResponse;
import com.user.task.service.UserService;
import com.user.task.service.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    List<UserResponseDto> getAll(@RequestParam String from, @RequestParam String to) {
        return userService.getAll(from, to)
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    UserResponseDto get(@PathVariable Long id) {
        return userMapper.mapToDto(userService.get(id));
    }

    @PostMapping
    UserResponseDto create(@RequestBody UserRequestDto requestDto) {
        return userMapper.mapToDto(userService.create(requestDto));
    }

    @PutMapping("/{id}")
    UserResponseDto update(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userMapper.mapToDto(userService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    UserResponseDto delete(@PathVariable Long id) {
        return userMapper.mapToDto(userService.delete(id));
    }

    @PatchMapping("/{id}")
    UserResponseDto patch(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userMapper.mapToDto(userService.patch(id, requestDto));
    }

    @ExceptionHandler(DataProcessingException.class)
    public ResponseEntity<ErrorResponse> handleException(DataProcessingException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
