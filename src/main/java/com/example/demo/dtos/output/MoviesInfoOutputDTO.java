package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviesInfoOutputDTO {
    private String id;
    private String title;
    private String director;
    private String overview;
    private int releaseYear;
    private String genre;
}
