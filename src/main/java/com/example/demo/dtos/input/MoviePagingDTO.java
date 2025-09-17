package com.example.demo.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoviePagingDTO {
    private MovieSearchDTO movie_search_dto;
    private PaginationDTO pagination_dto;
}
