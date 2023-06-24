package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.CustomerUsersDetailsService;
import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.JWT.JwtUtil;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.dto.converter.UserDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateUserRequest;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private CustomerUsersDetailsService customerUsersDetailsService;
    private JwtFilter jwtFilter;
    private JwtUtil jwtUtil;
    private UserDtoConverter userDtoConverter;
    @BeforeEach
    public void setUp(){
        userRepository = mock(UserRepository.class);
        authenticationManager = mock(AuthenticationManager.class);
        customerUsersDetailsService = mock(CustomerUsersDetailsService.class);
        jwtFilter = mock(JwtFilter.class);
        jwtUtil = mock(JwtUtil.class);
        userDtoConverter = mock(UserDtoConverter.class);

        userService =
                new UserService(
                        userRepository,
                        authenticationManager,
                        customerUsersDetailsService,
                        jwtUtil,
                        jwtFilter,
                        userDtoConverter);
    }


    @Test
    public void testRegister_whenUserEmailIsNotExist_shouldSuccessfullySavedUserAndReturnUserDto(){
        CreateUserRequest given = CreateUserRequest.builder()
                        .email("test-email")
                        .name("text-name")
                        .surname("text-surname")
                        .age(19)
                        .role("test-role")
                        .password("text-password")
                        .build();
        User user = User.builder()
                        .id(1)
                        .email("test-email")
                        .name("text-name")
                        .surname("text-surname")
                        .age(19)
                        .role("test-role")
                        .password("text-password")
                        .build();
        UserDto expected = UserDto.builder()
                        .id(1)
                        .email("text-email")
                        .name("text-name")
                        .surname("test-surname")
                        .age(19)
                        .gender("test-gender")
                        .role("test-email")
                        .build();


        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userDtoConverter.convert(any(User.class))).thenReturn(expected);

        UserDto actual = userService.register(given);

        assertEquals(expected, actual);

        verify(userRepository).findByEmail(any(String.class));
        verify(userRepository).save(any(User.class));
        verify(userDtoConverter).convert(any(User.class));
    }

    @Test
    public void testRegister_whenUserEmailIsExist_shouldFailedSavedUserAndReturnUserNotFoundException(){
        CreateUserRequest given = CreateUserRequest.builder()
                .email("test-email")
                .name("text-name")
                .surname("text-surname")
                .age(19)
                .role("test-role")
                .password("text-password")
                .build();
        User test = User.builder()
                .id(1)
                .email("test-email")
                .name("text-name")
                .surname("text-surname")
                .age(19)
                .role("test-role")
                .password("text-password")
                .build();

        UserNotFoundException excepted =
                new UserNotFoundException("Email already is taken ! ");

        when(userRepository.findByEmail(given.getEmail())).thenReturn(test);

        UserNotFoundException actual =
                assertThrows(UserNotFoundException.class,
                        ()-> userService.register(given));

        assertEquals(excepted.getMessage(), actual.getMessage());

        verify(userRepository).findByEmail(any(String.class));
    }

}