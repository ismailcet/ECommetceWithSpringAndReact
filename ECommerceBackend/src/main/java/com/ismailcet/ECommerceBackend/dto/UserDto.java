package com.ismailcet.ECommerceBackend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDto {
    private Integer id;
    private String email;
    private String name;
    private String surname;
    private Integer age;
    private String gender;
    private String role = "user";
}
