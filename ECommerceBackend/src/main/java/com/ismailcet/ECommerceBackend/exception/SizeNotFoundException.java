package com.ismailcet.ECommerceBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SizeNotFoundException extends RuntimeException{

    public SizeNotFoundException(String message) {
        super(message);
    }
}
