package com.user.task;

import com.user.task.config.AppConfig;
import com.user.task.dto.request.LocalAddressRequestDto;
import com.user.task.dto.request.UserRequestDto;
import com.user.task.exception.DataProcessingException;
import com.user.task.model.LocalAddress;
import com.user.task.model.User;
import com.user.task.repository.LocalAddressRepository;
import com.user.task.repository.UserRepository;
import com.user.task.service.UserService;
import com.user.task.service.mapper.LocalAddressMapper;
import com.user.task.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserTaskApplicationTests {
    private static final String DEFAULT_EMAIL = "default@eamil.com";
    private static final String DEFAULT_FIRST_NAME = "John";
    private static final String DEFAULT_LAST_NAME = "Johnson";
    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.parse("2000-04-20");

    @Autowired
    UserService userService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    AppConfig appConfig;
    @MockBean
    UserMapper userMapper;
    @MockBean
    LocalAddressRepository localAddressRepository;
    @MockBean
    LocalAddressMapper localAddressMapper;

    @Test
    public void create_Ok() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(DEFAULT_BIRTH_DATE);

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(appConfig.getRegisterAge()).thenReturn(18);

        userService.create(new UserRequestDto());

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void create_AddressCreation_Ok() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(DEFAULT_BIRTH_DATE);
        LocalAddressRequestDto address = new LocalAddressRequestDto();
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setAddress(address);

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(appConfig.getRegisterAge()).thenReturn(18);

        userService.create(requestDto);

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).save(any());
        verify(localAddressMapper, times(1)).mapToEntity(any());
        verify(localAddressRepository, times(1)).save(any());
    }

    @Test
    public void create_NullData_ExceptionThrown() {
        when(userMapper.mapToEntity(any())).thenReturn(new User());

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.create(new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Any of email, birth date, first name or last name fields can't be empty");
    }

    @Test
    public void create_WrongEmail_ExceptionThrown() {
        User user = new User();
        user.setEmail("WrongEmail");
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(DEFAULT_BIRTH_DATE);

        when(userMapper.mapToEntity(any())).thenReturn(user);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.create(new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Email WrongEmail is not valid");
    }

    @Test
    public void create_WrongBirthDate_ExceptionThrown() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(LocalDate.of(2030, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.create(new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Birth date must be earlier than current date");
    }

    @Test
    public void create_WrongAge_ExceptionThrown() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(LocalDate.of(2020, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(appConfig.getRegisterAge()).thenReturn(18);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.create(new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "User must be older than 18 years old");
    }

    @Test
    public void getAll_Ok() {
        String from = "2000-03-10";
        String to = "2010-03-10";

        userService.getAll(from, to);

        verify(userRepository, times(1)).findAllBetweenDates(any(), any());
    }

    @Test
    public void getAll_WrongRange_ExceptionThrown() {
        String from = "2010-03-10";
        String to = "2000-03-10";

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.getAll(from, to)).getMessage();
        assertEquals(errorMessage, "From date can't be after To date");
    }

    @Test
    public void update_Ok() {
        User oldUser = new User();
        oldUser.setEmail(DEFAULT_EMAIL);
        oldUser.setFirstName(DEFAULT_FIRST_NAME);
        oldUser.setLastName(DEFAULT_LAST_NAME);
        oldUser.setBirthDate(DEFAULT_BIRTH_DATE);
        User newUser = new User();
        newUser.setEmail("newDefault@email.com");
        newUser.setFirstName("Mark");
        newUser.setLastName("Markson");
        newUser.setBirthDate(LocalDate.of(2000, 12, 31));

        when(userMapper.mapToEntity(any())).thenReturn(newUser);
        when(appConfig.getRegisterAge()).thenReturn(18);
        when(userRepository.findById(any())).thenReturn(Optional.of(oldUser));

        userService.update(1L, new UserRequestDto());

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void update_AddressUpdate_Ok() {
        User oldUser = new User();
        oldUser.setEmail(DEFAULT_EMAIL);
        oldUser.setFirstName(DEFAULT_FIRST_NAME);
        oldUser.setLastName(DEFAULT_LAST_NAME);
        oldUser.setBirthDate(DEFAULT_BIRTH_DATE);
        User newUser = new User();
        newUser.setEmail("newDefault@email.com");
        newUser.setFirstName("Mark");
        newUser.setLastName("Markson");
        newUser.setBirthDate(LocalDate.of(2000, 12, 31));
        LocalAddress address = new LocalAddress();
        address.setHouseNumber(1L);
        address.setStreet("Street");
        newUser.setAddress(address);

        when(userMapper.mapToEntity(any())).thenReturn(newUser);
        when(appConfig.getRegisterAge()).thenReturn(18);
        when(userRepository.findById(any())).thenReturn(Optional.of(oldUser));

        userService.update(1L, new UserRequestDto());

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
        verify(localAddressRepository, times(1)).save(any());
    }

    @Test
    public void update_NullData_ExceptionThrown() {
        when(userMapper.mapToEntity(any())).thenReturn(new User());

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.update(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Any of email, birth date, first name or last name fields can't be empty");
    }

    @Test
    public void update_WrongEmail_ExceptionThrown() {
        User user = new User();
        user.setEmail("WrongEmail");
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(DEFAULT_BIRTH_DATE);

        when(userMapper.mapToEntity(any())).thenReturn(user);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.update(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Email WrongEmail is not valid");
    }

    @Test
    public void update_WrongBirthDate_ExceptionThrown() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(LocalDate.of(2030, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.update(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Birth date must be earlier than current date");
    }

    @Test
    public void update_WrongAge_ExceptionThrown() {
        User user = new User();
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setBirthDate(LocalDate.of(2020, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(appConfig.getRegisterAge()).thenReturn(18);

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.update(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "User must be older than 18 years old");
    }

    @Test
    public void patch_Ok() {
        User oldUser = new User();
        oldUser.setEmail(DEFAULT_EMAIL);
        oldUser.setFirstName(DEFAULT_FIRST_NAME);
        oldUser.setLastName(DEFAULT_LAST_NAME);
        oldUser.setBirthDate(DEFAULT_BIRTH_DATE);
        User newUser = new User();
        newUser.setEmail("newDefault@email.com");

        when(userMapper.mapToEntity(any())).thenReturn(newUser);
        when(appConfig.getRegisterAge()).thenReturn(18);
        when(userRepository.findById(any())).thenReturn(Optional.of(oldUser));

        userService.patch(1L, new UserRequestDto());

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void patch_AddressPatch_Ok() {
        User oldUser = new User();
        oldUser.setEmail(DEFAULT_EMAIL);
        oldUser.setFirstName(DEFAULT_FIRST_NAME);
        oldUser.setLastName(DEFAULT_LAST_NAME);
        oldUser.setBirthDate(DEFAULT_BIRTH_DATE);
        User newUser = new User();
        LocalAddress address = new LocalAddress();
        address.setHouseNumber(1L);
        address.setStreet("Street");
        newUser.setAddress(address);

        when(userMapper.mapToEntity(any())).thenReturn(newUser);
        when(appConfig.getRegisterAge()).thenReturn(18);
        when(userRepository.findById(any())).thenReturn(Optional.of(oldUser));

        userService.patch(1L, new UserRequestDto());

        verify(userMapper, times(1)).mapToEntity(any());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
        verify(localAddressRepository, times(1)).save(any());
    }

    @Test
    public void patch_WrongEmail_ExceptionThrown() {
        User user = new User();
        user.setEmail("WrongEmail");

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.patch(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Email WrongEmail is not valid");
    }

    @Test
    public void patch_WrongBirthDate_ExceptionThrown() {
        User user = new User();
        user.setBirthDate(LocalDate.of(2030, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.patch(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "Birth date must be earlier than current date");
    }

    @Test
    public void patch_WrongAge_ExceptionThrown() {
        User user = new User();
        user.setBirthDate(LocalDate.of(2020, 3, 10));

        when(userMapper.mapToEntity(any())).thenReturn(user);
        when(appConfig.getRegisterAge()).thenReturn(18);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        String errorMessage = assertThrows(DataProcessingException.class,
                () -> userService.patch(1L, new UserRequestDto())).getMessage();
        assertEquals(errorMessage, "User must be older than 18 years old");
    }
}
