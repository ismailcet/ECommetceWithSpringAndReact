package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.CustomerUsersDetailsService;
import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.JWT.JwtUtil;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.dto.converter.UserDtoConverter;
import com.ismailcet.ECommerceBackend.dto.request.CreateUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.LoginUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateUserRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllUsersResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetUserByUserId;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.exception.AuthenticationException;
import com.ismailcet.ECommerceBackend.exception.UserNotFoundException;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import com.ismailcet.ECommerceBackend.utils.PasswordUtils;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerUsersDetailsService customerUsersDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final UserDtoConverter userDtoConverter;


    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, CustomerUsersDetailsService customerUsersDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter, UserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUsersDetailsService = customerUsersDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
        this.userDtoConverter = userDtoConverter;
    }

    public UserDto register(CreateUserRequest createUserRequest){
        try{
            User userTest = userRepository.findByEmail(createUserRequest.getEmail());
            if(Objects.isNull(userTest)){
                User user =
                        User.builder()
                                .name(createUserRequest.getName())
                                .surname(createUserRequest.getSurname())
                                .age(createUserRequest.getAge())
                                .gender(createUserRequest.getGender())
                                .role(createUserRequest.getRole())
                                .email(createUserRequest.getEmail())
                                .password(PasswordUtils.hashPassword(createUserRequest.getPassword()))
                                .build();

                userRepository.save(user);
                return userDtoConverter.convert(user);
            }else{
                throw new UserNotFoundException("Email already is taken ! ");
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    public String login(LoginUserRequest loginUserRequest){
        log.info("Inside Login {}" + loginUserRequest);
        try{
            User user = userRepository.findByEmail(loginUserRequest.getEmail());
            if(!Objects.isNull(user) ){
                    if(PasswordUtils.verifyPassword(loginUserRequest.getPassword(),user.getPassword())){
                        Authentication auth =
                                authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),loginUserRequest.getPassword())
                                );
                        if(auth.isAuthenticated()){

                            return "{\"token\":\"" +
                                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),customerUsersDetailsService.getUserDetail().getRole())
                                    +"\"}";
                        }
                        throw new UserNotFoundException("Wrong Credentials ! ");
                    }else{
                        throw new UserNotFoundException("Wrong Password");
                    }
            }
            throw new UserNotFoundException("Wrong Email ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public List<GetAllUsersResponse> getAllUser(){
        try{
            List<GetAllUsersResponse> users = new ArrayList<>();
            if(jwtFilter.isAdmin()){
                users = userRepository.findAll().stream()
                                .map(user->GetAllUsersResponse.builder()
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .name(user.getName())
                                        .surname(user.getEmail())
                                        .gender(user.getGender())
                                        .age(user.getAge())
                                        .role(user.getRole())
                                        .build())
                                .collect(Collectors.toList());
                return users;
            }
            throw new AuthenticationException("unauthorized access !");
        }catch (Exception ex){
            throw ex;
        }
    }

    public GetUserByUserId getUserByUserId(Integer id) {
        try{
            if(jwtFilter.isUser()){
                Optional<User> findUser =
                        userRepository.findById(id);
                if(findUser.isPresent()){
                    GetUserByUserId user =
                            GetUserByUserId.builder()
                                    .id(findUser.get().getId())
                                    .email(findUser.get().getEmail())
                                    .name(findUser.get().getName())
                                    .surname(findUser.get().getSurname())
                                    .gender(findUser.get().getGender())
                                    .age(findUser.get().getAge())
                                    .role(findUser.get().getRole())
                                    .build();
                    return user;
                }
                throw new UserNotFoundException("User Not Found ! ");
            }
            throw new AuthenticationException("unauthorized access ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public UserDto updateUserByUserId(Integer id, UpdateUserRequest updateUserRequest) {
        try{
            if(customerUsersDetailsService.getUserDetail().getId() == id){
                Optional<User> findUser =
                        userRepository.findById(id);
                if(findUser.isPresent()){
                    findUser.get().setName(updateUserRequest.getName());
                    findUser.get().setSurname(updateUserRequest.getSurname());
                    findUser.get().setPassword(PasswordUtils.hashPassword(updateUserRequest.getPassword()));
                    findUser.get().setEmail(updateUserRequest.getEmail());
                    findUser.get().setGender(updateUserRequest.getGender());
                    findUser.get().setAge(updateUserRequest.getAge());
                    userRepository.save(findUser.get());
                    return userDtoConverter.convert(findUser.get());
                }
                throw new UserNotFoundException("User Not Found ! ");
            }
            throw new AuthenticationException("unauthorized access ! ");
        }catch (Exception ex){
            throw ex;
        }
    }

    public String deleteUserByUserId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> user =
                        userRepository.findById(id);
                if(user.isPresent()){
                    userRepository.deleteById(id);
                    return "User Successfully Deleted !";
                }
            throw new UserNotFoundException("User Id does not id");
            }
            throw new AuthenticationException("Unauthorized access. ! ");
        }catch (Exception ex){
            throw ex;
        }
    }
}
