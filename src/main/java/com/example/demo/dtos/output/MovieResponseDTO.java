package com.example.demo.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponseDTO {
    private String id;
    private String title;
    private String overview;
    private Set<String> genres;
    private Set<String> cast;
    private Set<String> directors;
    private Set<String> recommended_movie_ids;
    private String trailer_path;
    private String poster_path;
}
