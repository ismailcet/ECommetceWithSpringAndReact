package com.ismailcet.ECommerceBackend.exception;

import com.ismailcet.ECommerceBackend.entity.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GeneralExceptionAdviser {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(UserNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ExceptionResponse response =
                new ExceptionResponse(
                  LocalDateTime.now(),
                  HttpStatus.NOT_FOUND,
                  "Employee Not Found ! ",
                  details);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handlerProductNotFoundException(ProductNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ExceptionResponse response =
                new ExceptionResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        "Product Not Found ! ",
                        details);

        return new ResponseEntity<>(response, response.getStatus());
    }
    @ExceptionHandler(SizeNotFoundException.class)
    public ResponseEntity<Object> handlerSizeNotFoundException(SizeNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ExceptionResponse response =
                new ExceptionResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        "Size Not Found ! ",
                        details);

        return new ResponseEntity<>(response, response.getStatus());
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handlerCategoryNotFoundException(CategoryNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ExceptionResponse response =
                new ExceptionResponse(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        "Category Not Found ! ",
                        details);

        return new ResponseEntity<>(response, response.getStatus());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handlerAuthenticationNotFoundException(AuthenticationException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        ExceptionResponse response =
                new ExceptionResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED,
                        "Unauthorized Access! ",
                        details);

        return new ResponseEntity<>(response, response.getStatus());
    }

}
