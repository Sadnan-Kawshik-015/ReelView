package com.example.demo.dtos.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieSearchDTO {
    private String title;
    private String director;
    private Integer releaseYear;
    private String genre;
    private List<String> tags;
    private List<String> cast;
}
