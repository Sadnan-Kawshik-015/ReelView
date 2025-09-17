package com.example.demo.dtos.output;

import com.example.demo.dtos.input.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviesPaginationResponseDTO {
    List<MoviesInfoOutputDTO> lists;
    PaginationOutputDTO pagination_dto;
}
