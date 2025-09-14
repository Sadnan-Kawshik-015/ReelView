package com.example.demo.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private String mobile_number;
    private String role_id;
}
