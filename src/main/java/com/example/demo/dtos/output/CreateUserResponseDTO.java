package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponseDTO {
    private String user_id;
    private String email;
    private String first_name;
    private String last_name;
    private String mobile_number;
    private String role_id;
}
