package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String email;
    private String full_name;
    private String mobile_number;
    private String role_id;
}
