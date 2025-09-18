package com.example.demo.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateReviewDTO {
    private String reviewId;
    private String reviewBody;
}
