package com.example.demo.dtos.input;

import com.example.demo.utils.customvalidation.annotation.MobileNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String first_name;
    private String last_name;
    @NotBlank
    private String password;
    @NotBlank
    private String mobile_number;
    @NotBlank
    private String role_id;
}
