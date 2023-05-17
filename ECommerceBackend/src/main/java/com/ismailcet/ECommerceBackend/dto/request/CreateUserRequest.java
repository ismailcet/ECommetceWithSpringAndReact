package com.ismailcet.ECommerceBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private Integer age;
    private String gender;
    private String role = "user";
}
