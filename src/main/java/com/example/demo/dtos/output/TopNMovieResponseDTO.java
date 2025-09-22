package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopNMovieResponseDTO {
    private String id;
    private String title;
    private String poster_path;
    private Double popularity;
    private String trailer_path;
    private String overview;
    private Set<String> genres;
    private String backdrop_path;
}
