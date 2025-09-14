package com.example.demo.dtos.output;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldValidationErrorDTO {
    private String field_name;
    private String error_message;
    private String rejected_value;

}
