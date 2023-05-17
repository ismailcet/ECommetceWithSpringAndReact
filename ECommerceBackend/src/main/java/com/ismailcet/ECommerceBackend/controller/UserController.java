package com.ismailcet.ECommerceBackend.controller;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.dto.request.CreateUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.LoginUserRequest;
import com.ismailcet.ECommerceBackend.dto.request.UpdateUserRequest;
import com.ismailcet.ECommerceBackend.dto.response.GetAllUsersResponse;
import com.ismailcet.ECommerceBackend.dto.response.GetUserByUserId;
import com.ismailcet.ECommerceBackend.entity.User;
import com.ismailcet.ECommerceBackend.service.UserService;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserRequest createUserRequest){
        try{
            return userService.register(createUserRequest);
        }catch (Exception er){
            er.printStackTrace();
        }
        return SystemUtils.getResponseEntityForUser(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserRequest loginUserRequest){
        try{
            return userService.login(loginUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return SystemUtils.getResponseEntity(SystemConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping()
    public ResponseEntity<List<GetAllUsersResponse>> getAllUser(){
        try{
            return userService.getAllUser();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserByUserId> getUserByUserId(@PathVariable("id") Integer id){
        try{
            return userService.getUserByUserId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new GetUserByUserId(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserByUserId(@PathVariable("id") Integer id, @RequestBody UpdateUserRequest updateUserRequest){
        try{
            return userService.updateUserByUserId(id, updateUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable("id") Integer id){
        try{
            return userService.deleteUserByUserId(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
