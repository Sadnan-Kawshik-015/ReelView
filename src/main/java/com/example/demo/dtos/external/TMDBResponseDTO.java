package com.example.demo.dtos.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBResponseDTO {
    private Boolean adult;
    private String overview;
    private String poster_path;
    private Integer runtime;
    private BigDecimal rating;
    private TMDBVideoDTO videos;
}
