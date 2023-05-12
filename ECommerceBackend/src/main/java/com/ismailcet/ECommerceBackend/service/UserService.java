package com.ismailcet.ECommerceBackend.service;

import com.ismailcet.ECommerceBackend.constants.SystemConstants;
import com.ismailcet.ECommerceBackend.model.User;
import com.ismailcet.ECommerceBackend.repository.UserRepository;
import com.ismailcet.ECommerceBackend.utils.SystemUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> register(User user){
        try{
            User userTest = userRepository.findByEmail(user.getEmail());
            if(Objects.isNull(userTest)){
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
}
