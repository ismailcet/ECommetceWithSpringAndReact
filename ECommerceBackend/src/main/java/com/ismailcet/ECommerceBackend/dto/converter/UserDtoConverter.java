package com.ismailcet.ECommerceBackend.dto.converter;

import com.ismailcet.ECommerceBackend.dto.UserDto;
import com.ismailcet.ECommerceBackend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public UserDto convert(User user){
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .gender(user.getGender())
                .age(user.getAge())
                .role(user.getRole())
                .build();
        return userDto;
    }
}
