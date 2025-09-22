package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieStatDTO {
    private Integer totalMovies;
    private Integer totalUniqueGenres;
    private Integer totalReviews;
    private Integer totalUsers;
}
