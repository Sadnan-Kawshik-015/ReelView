package com.example.demo.dtos.helper;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenDTO {
    private String user_id;
    private String token_type;
}
