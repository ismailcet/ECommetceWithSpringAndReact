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
            return new ResponseEntity<>( userService.register(createUserRequest),
                    HttpStatus.CREATED
                    );
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserRequest loginUserRequest){
            return ResponseEntity.ok(userService.login(loginUserRequest));
    }

    @GetMapping()
    public ResponseEntity<List<GetAllUsersResponse>> getAllUser(){
            return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserByUserId> getUserByUserId(@PathVariable("id") Integer id){
            return ResponseEntity.ok(userService.getUserByUserId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserByUserId(@PathVariable("id") Integer id, @RequestBody UpdateUserRequest updateUserRequest){
            return ResponseEntity.ok(userService.updateUserByUserId(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable("id") Integer id){
        userService.deleteUserByUserId(id);
        return ResponseEntity.noContent().build();
    }


}
