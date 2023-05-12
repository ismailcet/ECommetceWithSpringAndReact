package com.ismailcet.ECommerceBackend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SystemUtils {

    public static ResponseEntity<String> getResponseEntity(String message , HttpStatus status){
        return new ResponseEntity<>("{\"message: \": \"" + message +"\"}",status);
    }
}
