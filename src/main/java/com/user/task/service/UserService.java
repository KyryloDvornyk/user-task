package com.user.task.service;

import com.user.task.config.AppConfig;
import com.user.task.dto.request.LocalAddressRequestDto;
import com.user.task.dto.request.UserRequestDto;
import com.user.task.exception.DataProcessingException;
import com.user.task.model.LocalAddress;
import com.user.task.model.User;
import com.user.task.repository.LocalAddressRepository;
import com.user.task.repository.UserRepository;
import com.user.task.service.mapper.LocalAddressMapper;
import com.user.task.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.user.task.util.ValidationUtils.isNot;
import static com.user.task.validation.UserValidation.*;
import static java.util.Objects.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LocalAddressRepository localAddressRepository;
    private final LocalAddressMapper localAddressMapper;
    private final UserMapper userMapper;
    private final AppConfig appConfig;

    UserService(UserRepository userRepository, LocalAddressRepository localAddressRepository,
                LocalAddressMapper localAddressMapper, UserMapper userMapper, AppConfig appConfig) {
        this.userRepository = userRepository;
        this.localAddressRepository = localAddressRepository;
        this.localAddressMapper = localAddressMapper;
        this.userMapper = userMapper;
        this.appConfig = appConfig;
    }

    public User create(UserRequestDto requestDto) {
        User newUser = userMapper.mapToEntity(requestDto);
        fullUserValidation(newUser);
        checkRegisterAge(newUser.getBirthDate());
        checkNewUserAddress(newUser, requestDto.getAddress());
        return userRepository.save(newUser);
    }

    private void checkNewUserAddress(User newUser, LocalAddressRequestDto address) {
        if (nonNull(address)) {
            LocalAddress savedAddress = localAddressRepository.save(localAddressMapper.mapToEntity(address));
            newUser.setAddress(savedAddress);
        }
    }

    private void checkRegisterAge(LocalDate birthDate) {
        Integer userAge = countUserAge(birthDate);
        Integer registerAge = appConfig.getRegisterAge();
        if (userAge < registerAge) {
            throw new DataProcessingException("User must be older than " + registerAge + " years old");
        }
    }

    private Integer countUserAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        Integer count = now.getYear() - birthDate.getYear();
        if (now.getMonth().getValue() < birthDate.getMonth().getValue()) {
            count--;
        } else if (now.getMonth().getValue() == birthDate.getMonth().getValue()
                && now.getDayOfMonth() < birthDate.getDayOfMonth()) {
            count--;
        }
        return count;
    }

    public List<User> getAll(String from, String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        dateRangeValidation(fromDate, toDate);
        return userRepository.findAllBetweenDates(fromDate, toDate);
    }

    public User get(Long id) {
        return getSavedUser(id);
    }

    private User getSavedUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataProcessingException("There is no such user with id " + id));
    }

    public User update(Long id, UserRequestDto requestDto) {
        User user = userMapper.mapToEntity(requestDto);
        fullUserValidation(user);
        checkRegisterAge(user.getBirthDate());
        User oldUser = getSavedUser(id);
        return userRepository.save(updateUserFields(oldUser, user));
    }

    private User updateUserFields(User oldUser, User newUser) {
        oldUser.setEmail(newUser.getEmail());
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setBirthDate(newUser.getBirthDate());
        if (nonNull(newUser.getAddress()) && isNot(newUser.getAddress().equals(oldUser.getAddress()))) {
            LocalAddress address = new LocalAddress();
            address.setStreet(newUser.getAddress().getStreet());
            address.setHouseNumber(newUser.getAddress().getHouseNumber());
            oldUser.setAddress(localAddressRepository.save(address));
        }
        oldUser.setPhoneNumber(newUser.getPhoneNumber());
        return oldUser;
    }

    public User delete(Long id) {
        User user = getSavedUser(id);
        userRepository.delete(user);
        return user;
    }

    public User patch(Long id, UserRequestDto requestDto) {
        User oldUser = getSavedUser(id);
        User newUser = userMapper.mapToEntity(requestDto);
        patchEmail(oldUser, newUser);
        patchName(oldUser, newUser);
        patchBirthDate(oldUser, newUser);
        patchAddress(oldUser, newUser);
        patchPhoneNumber(oldUser, newUser);
        return userRepository.save(oldUser);
    }

    private void patchEmail(User oldUser, User newUser) {
        if (nonNull(newUser.getEmail())) {
            emailValidation(newUser.getEmail());
            oldUser.setEmail(newUser.getEmail());
        }
    }

    private void patchName(User oldUser, User newUser) {
        if (nonNull(newUser.getFirstName())) {
            oldUser.setFirstName(newUser.getFirstName());
        }
        if (nonNull(newUser.getLastName())) {
            oldUser.setLastName(newUser.getLastName());
        }
    }

    private void patchBirthDate(User oldUser, User newUser) {
        if (nonNull(newUser.getBirthDate())) {
            birthDateValidation(newUser.getBirthDate());
            checkRegisterAge(newUser.getBirthDate());
            oldUser.setBirthDate(newUser.getBirthDate());
        }
    }

    private void patchAddress(User oldUser, User newUser) {
        if (nonNull(newUser.getAddress())
                && isNot(newUser.getAddress().equals(oldUser.getAddress()))) {
            LocalAddress address = new LocalAddress();
            address.setStreet(newUser.getAddress().getStreet());
            address.setHouseNumber(newUser.getAddress().getHouseNumber());
            oldUser.setAddress(localAddressRepository.save(address));

        }
    }

    private void patchPhoneNumber(User oldUser, User newUser) {
        if (nonNull(newUser.getPhoneNumber())) {
            oldUser.setPhoneNumber(newUser.getPhoneNumber());
        }
    }
}
