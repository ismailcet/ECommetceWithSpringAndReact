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

    public ResponseEntity<UserDto> register(CreateUserRequest createUserRequest){
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
                return SystemUtils.getResponseEntityForUser(userDtoConverter.convert(user) , HttpStatus.OK);
            }else{
                return SystemUtils.getResponseEntityForUser(null,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntityForUser(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(LoginUserRequest loginUserRequest){
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

                            return new ResponseEntity<String>(
                                    "{\"token\":\"" +
                                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),customerUsersDetailsService.getUserDetail().getRole())
                                    +"\"}",HttpStatus.OK);
                        }
                        return SystemUtils.getResponseEntity("wrong",HttpStatus.BAD_REQUEST);
                    }else{
                        return SystemUtils.getResponseEntity("Wrong Password",HttpStatus.BAD_REQUEST);
                    }
            }
            return SystemUtils.getResponseEntity("Wrong Email",HttpStatus.BAD_REQUEST);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<GetAllUsersResponse>> getAllUser(){
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
                return new ResponseEntity<>(users,HttpStatus.OK);
            }
            return new ResponseEntity<>(users,HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<GetUserByUserId> getUserByUserId(Integer id) {
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
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new GetUserByUserId(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<UserDto> updateUserByUserId(Integer id, UpdateUserRequest updateUserRequest) {
        try{
            if(jwtFilter.isUser()){
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
                    return new ResponseEntity<>(userDtoConverter.convert(findUser.get()),HttpStatus.OK);
                }
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> deleteUserByUserId(Integer id) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> user =
                        userRepository.findById(id);
                if(user.isPresent()){
                    userRepository.deleteById(id);
                    return new ResponseEntity<>("User Successfully Deleted !" ,HttpStatus.OK);
                }
                return new ResponseEntity<>("User Id does not id", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Unauthorized access.", HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
