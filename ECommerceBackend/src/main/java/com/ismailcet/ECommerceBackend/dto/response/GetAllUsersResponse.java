package com.ismailcet.ECommerceBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllUsersResponse {
    private Integer id;
    private String email;
    private String name;
    private String surname;
    private Integer age;
    private String gender;
    private String role;
}
