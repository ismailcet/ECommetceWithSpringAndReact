package com.ismailcet.ECommerceBackend.utils;

import com.ismailcet.ECommerceBackend.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SystemUtils {

    public static ResponseEntity<String> getResponseEntity(String message , HttpStatus status){
        return new ResponseEntity<>("{\"message: \": \"" + message +"\"}",status);
    }
    public static ResponseEntity<UserDto> getResponseEntityForUser(UserDto userDto, HttpStatus status){
        return new ResponseEntity<>(userDto,status);
    }


}
