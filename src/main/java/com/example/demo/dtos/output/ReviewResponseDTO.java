package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private String reviewId;
    private String reviewBody;
    private String authorId;
    private String author;
    private String email;
    private String createdAt;
}
