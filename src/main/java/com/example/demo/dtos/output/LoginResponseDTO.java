package com.example.demo.dtos.output;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class LoginResponseDTO {
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String token_type;
}
