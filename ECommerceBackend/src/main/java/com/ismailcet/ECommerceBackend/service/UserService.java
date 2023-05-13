package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.JWT.CustomerUsersDetailsService;
import com.ismailcet.ECommerceBackend.JWT.JwtFilter;
import com.ismailcet.ECommerceBackend.JWT.JwtUtil;
import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.model.User;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerUsersDetailsService customerUsersDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;


    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, CustomerUsersDetailsService customerUsersDetailsService, JwtUtil jwtUtil, JwtFilter jwtFilter) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUsersDetailsService = customerUsersDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
    }

    public ResponseEntity<String> register(User user){
        try{
            User userTest = userRepository.findByEmail(user.getEmail());
            if(Objects.isNull(userTest)){
                String password = PasswordUtils.hashPassword(user.getPassword());
                user.setPassword(password);
                userRepository.save(user);
                return SystemUtils.getResponseEntity("Succesfully Registered . " , HttpStatus.OK);
            }else{
                return SystemUtils.getResponseEntity("Email Already Exits",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(Map<String, String> requestMap){
        log.info("Inside Login {}" + requestMap);
        try{
            User user = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) ){
                    if(PasswordUtils.verifyPassword(requestMap.get("password"),user.getPassword())){
                        Authentication auth =
                                authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
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

    public ResponseEntity<List<User>> getAllUser(){
        try{
            List<User> userList = new ArrayList<>();

            if(jwtFilter.isAdmin()){
                userList = userRepository.findAll();
                return new ResponseEntity<>(userList,HttpStatus.OK);
            }
            return new ResponseEntity<>(userList,HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
