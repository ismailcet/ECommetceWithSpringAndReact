package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.CustomerUsersDetailsService;
import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.JWT.JwtUtil;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.dto.converter.UserDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.LoginUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateUserRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllUsersResponse;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import com.ismailcet.ECommerceBackend.utils.PasswordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                        .name("test-name")
                        .surname("test-surname")
                        .age(19)
                        .role("test-role")
                        .password("test-password")
                        .build();
        User user = User.builder()
                        .id(1)
                        .email("test-email")
                        .name("test-name")
                        .surname("test-surname")
                        .age(19)
                        .role("test-role")
                        .password("test-password")
                        .build();
        UserDto expected = UserDto.builder()
                        .id(1)
                        .email("test-email")
                        .name("test-name")
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
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();
        User test = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();

        UserNotFoundException excepted =
                new UserNotFoundException("Email already is taken ! ");

        when(userRepository.findByEmail(given.getEmail())).thenReturn(test);

        UserNotFoundException actual =
                assertThrows(UserNotFoundException.class,
                        ()-> userService.register(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findByEmail(any(String.class));
    }
    @Test
    public void testLogin_whenEmailAndPasswordAreCorrect_shouldReturnStringToken(){
        LoginUserRequest given = LoginUserRequest.builder()
                .email("test-email")
                .password("test-password")
                .build();

        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();
        TestingAuthenticationToken token =
                new TestingAuthenticationToken(given.getEmail(), given.getPassword());
        token.setAuthenticated(true);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(given.getEmail(), given.getPassword());
        String expected = "{\"token\":\"test-token\"}";

        when(userRepository.findByEmail(given.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(auth)).thenReturn(token);
        when(customerUsersDetailsService.getUserDetail()).thenReturn(user);
        when(jwtUtil.generateToken("test-email", "test-role")).thenReturn("test-token");

        String actual = userService.login(given);

        assertEquals(expected, actual);

        verify(userRepository).findByEmail(any(String.class));
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(customerUsersDetailsService, new Times(2)).getUserDetail();
        verify(jwtUtil).generateToken(any(String.class), any(String.class));

    }
    @Test
    public void testLogin_whenEmailIsNotCorrect_shouldThrowUserNotFoundException(){
        LoginUserRequest given = LoginUserRequest.builder()
                .email("test-email")
                .password("test-password")
                .build();

        UserNotFoundException excepted =
                new UserNotFoundException("Wrong Email ! ");

        when(userRepository.findByEmail(given.getEmail())).thenReturn(null);

        UserNotFoundException actual = assertThrows(UserNotFoundException.class,
                () -> userService.login(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findByEmail(any(String.class));
    }
    @Test
    public void testLogin_whenEmailIsCorrectPasswordIsNotEquals_shouldThrowUserNotFoundException(){
        LoginUserRequest given = LoginUserRequest.builder()
                .email("test-email")
                .password("test-password")
                .build();
        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password(PasswordUtils.hashPassword("test-wrong"))
                .build();

        UserNotFoundException excepted =
                new UserNotFoundException("Wrong Password");

        when(userRepository.findByEmail(given.getEmail())).thenReturn(user);
        UserNotFoundException actual = assertThrows(UserNotFoundException.class,
                ()->userService.login(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findByEmail(any(String.class));
    }
    @Test
    public void testLogin_whenEmailAndPasswordAreCorrectAuthIsAuthenticatedFalse_shouldThrowUserNotFoundException(){
        LoginUserRequest given = LoginUserRequest.builder()
                .email("test-email")
                .password("test-password")
                .build();

        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();
        TestingAuthenticationToken token =
                new TestingAuthenticationToken(given.getEmail(), given.getPassword());
        token.setAuthenticated(false);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(given.getEmail(), given.getPassword());

        UserNotFoundException excepted = new UserNotFoundException("Wrong Credentials ! ");

        when(userRepository.findByEmail(given.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(auth)).thenReturn(token);

        UserNotFoundException actual = assertThrows(UserNotFoundException.class,
                ()->userService.login(given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findByEmail(any(String.class));
        verify(authenticationManager).authenticate(any(Authentication.class));

    }
    @Test
    public void testGetAllUser_whenUserRoleIsAdmin_shouldReturnListOfUserDto(){
        User user1 = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();
        User user2 = User.builder()
                .id(2)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();
        List<User> result = new ArrayList<>(List.of(user1, user2));

        UserDto userDto1 = UserDto.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .gender("test-gender")
                .role("test-role")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .gender("test-gender")
                .role("test-role")
                .build();
        List<UserDto> excepted = new ArrayList<>(List.of(userDto1, userDto2));

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.findAll()).thenReturn(result);
        when(userDtoConverter.convert(user1)).thenReturn(userDto1);
        when(userDtoConverter.convert(user2)).thenReturn(userDto2);

        List<UserDto> actual = userService.getAllUser();

        assertEquals(excepted, actual);
        assertEquals(excepted.size(), actual.size());

        verify(jwtFilter).isAdmin();
        verify(userRepository).findAll();
        verify(userDtoConverter, new Times(result.size())).convert(any(User.class));
    }
    @Test
    public void testGetAllUser_whenUserRoleIsNotAdmin_shouldThrowAuthenticationException(){
        AuthenticationException excepted =
                new AuthenticationException("unauthorized access !");

        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class ,
                ()-> userService.getAllUser());

        assertEquals(excepted.getMessage(), actual.getMessage());

        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testGetUserByUserId_whenUserIdIsExistAndUserEmailIsEqualTokenEmail_shouldReturnUserDto(){
        Integer id = 1;
        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();

        UserDto excepted = UserDto.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .gender("test-gender")
                .role("test-role")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(jwtFilter.getCurrentUser()).thenReturn("test-email");
        when(userDtoConverter.convert(user)).thenReturn(excepted);

        UserDto actual = userService.getUserByUserId(id);

        assertEquals(excepted, actual);
        assertEquals(id, actual.getId());

        verify(jwtFilter).getCurrentUser();
        verify(userRepository).findById(any(Integer.class));
        verify(userDtoConverter).convert(any(User.class));
    }
    @Test
    public void testGetUserByUserId_whenUserIdIsNotExist_shouldThrowUserNotFoundException(){
        Integer id = 1;
        UserNotFoundException excepted =
                new UserNotFoundException("User Not Found ! ");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class ,
                ()-> userService.getUserByUserId(id));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findById(any(Integer.class));
    }
    @Test
    public void testGetUserByUserId_whenUserIdIsExistAndUserEmailIsNotEqualsTokenEmail_shouldThrowAuthenticationException(){
        Integer id = 1;
        String tokenEmail = "test-wrong";

        User user = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .password("test-password")
                .build();

        AuthenticationException excepted =
                new AuthenticationException("unauthorized access ! ");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(jwtFilter.getCurrentUser()).thenReturn(tokenEmail);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()->userService.getUserByUserId(id));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(userRepository).findById(any(Integer.class));
        verify(jwtFilter).getCurrentUser();
    }
    @Test
    public void testUpdateUserByUserId_whenUserIdEqualsTokenUserIdAndUserIdIsExist_shouldUserSuccessfullyUpdatedAndReturnUserDto(){
        Integer id = 1;
        User test = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .gender("test-gender")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();

        UpdateUserRequest given = UpdateUserRequest.builder()
                .email("test-email-upd")
                .name("test-name-upd")
                .surname("test-surname-upd")
                .age(19)
                .password(PasswordUtils.hashPassword("test-password"))
                .gender("test-gender-upd")
                .build();

        User result = User.builder()
                .id(1)
                .email("test-email-upd")
                .name("test-name-upd")
                .surname("test-surname-upd")
                .age(19)
                .role("test-role")
                .gender("test-gender-upd")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();

        UserDto excepted = UserDto.builder()
                .id(1)
                .email("test-email-upd")
                .name("test-name-upd")
                .surname("test-surname-upd")
                .age(19)
                .gender("test-gender-upd")
                .role("test-role")
                .build();

        when(customerUsersDetailsService.getUserDetail()).thenReturn(test);
        when(userRepository.findById(id)).thenReturn(Optional.of(test));
        when(userRepository.save(any(User.class))).thenReturn(result);
        when(userDtoConverter.convert(test)).thenReturn(excepted);

        UserDto actual = userService.updateUserByUserId(id, given);

        assertEquals(excepted , actual);
        assertEquals(excepted.getName(), actual.getName());
        assertEquals(result.getEmail(), actual.getEmail());

        verify(customerUsersDetailsService).getUserDetail();
        verify(userRepository).findById(any(Integer.class));
        verify(userRepository).save(any(User.class));
        verify(userDtoConverter).convert(any(User.class));
    }
    @Test
    public void testUpdateUserByUserId_whenUserIdIsNotEqualsTokenUserId_shouldThrowAuthenticationException(){
        Integer id = 1;
        User test = User.builder()
                .id(2)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .gender("test-gender")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();

        UpdateUserRequest given = UpdateUserRequest.builder()
                .email("test-email-upd")
                .name("test-name-upd")
                .surname("test-surname-upd")
                .age(19)
                .password(PasswordUtils.hashPassword("test-password"))
                .gender("test-gender-upd")
                .build();

        AuthenticationException excepted =
                new AuthenticationException("unauthorized access ! ");

        when(customerUsersDetailsService.getUserDetail()).thenReturn(test);

        AuthenticationException actual = assertThrows(AuthenticationException.class ,
                ()-> userService.updateUserByUserId(id, given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(customerUsersDetailsService).getUserDetail();
    }
    @Test
    public void testUpdateUserByUserId_whenUserIdIsEqualsTokenUserIdAndUserIdIsNotExist_shouldThrowUserNotFoundException(){
        Integer id = 1;
        User test = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .gender("test-gender")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();

        UpdateUserRequest given = UpdateUserRequest.builder()
                .email("test-email-upd")
                .name("test-name-upd")
                .surname("test-surname-upd")
                .age(19)
                .password(PasswordUtils.hashPassword("test-password"))
                .gender("test-gender-upd")
                .build();
        UserNotFoundException excepted =
                new UserNotFoundException("User Not Found ! ");

        when(customerUsersDetailsService.getUserDetail()).thenReturn(test);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class,
                ()->userService.updateUserByUserId(id, given));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(customerUsersDetailsService).getUserDetail();
        verify(userRepository).findById(any(Integer.class));
    }
    @Test
    public void testDeleteUserByUserId_whenUserIdIsExistAndUserRoleIsAdmin_whenSuccessfullyDeleted(){
        Integer id = 1;
        User test = User.builder()
                .id(1)
                .email("test-email")
                .name("test-name")
                .surname("test-surname")
                .age(19)
                .role("test-role")
                .gender("test-gender")
                .password(PasswordUtils.hashPassword("test-password"))
                .build();
        String excepted = "User Successfully Deleted !";

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(test));

        String actual = userService.deleteUserByUserId(id);

        assertEquals(excepted, actual);
        assertTrue(actual.equals(excepted));

        verify(jwtFilter).isAdmin();
        verify(userRepository).findById(any(Integer.class));
    }
    @Test
    public void testDeleteUserByUserId_whenUserIdIsExistAndUserRoleIsNotAdmin_whenThrowAuthenticationException(){
        Integer id = 1;
        AuthenticationException excepted =
                new AuthenticationException("Unauthorized access. ! ");
        when(jwtFilter.isAdmin()).thenReturn(false);

        AuthenticationException actual = assertThrows(AuthenticationException.class,
                ()-> userService.deleteUserByUserId(id));

        assertEquals(excepted.getClass(), actual.getClass());
        assertEquals(excepted.getMessage(), actual.getMessage());


        verify(jwtFilter).isAdmin();
    }
    @Test
    public void testDeleteUserByUserId_whenUserIdIsNotExistAndUserRoleIsAdmin_whenThrowUserNotFoundException(){
        Integer id = 1;
        UserNotFoundException excepted =
                new UserNotFoundException("User Id does not id");

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException actual = assertThrows(UserNotFoundException.class,
                ()-> userService.deleteUserByUserId(id));

        assertEquals(excepted.getMessage(), actual.getMessage());
        assertEquals(excepted.getClass(), actual.getClass());

        verify(jwtFilter).isAdmin();
        verify(userRepository).findById(any(Integer.class));

    }
}