package com.example.demo.service;

import com.example.demo.dtos.external.TMDBResponseDTO;
import com.example.demo.dtos.external.TMDBVideoDTO;
import com.example.demo.dtos.external.TMDBVideoResultsDTO;
import com.example.demo.dtos.input.MoviePagingDTO;
import com.example.demo.dtos.output.MovieResponseDTO;
import com.example.demo.dtos.output.MoviesPaginationResponseDTO;
import com.example.demo.entities.mongoEntities.Movies;
import com.example.demo.repositories.mongoRepositories.MovieRepository;
import com.example.demo.utils.StringUtils;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService extends BaseService {
    private final MovieRepository movieRepository;
    private final TMDBDataService dataService;

    public MoviesPaginationResponseDTO getMyMoviesWithPagination(MoviePagingDTO pagingDTO) {
        return new MoviesPaginationResponseDTO();
    }


    public MovieResponseDTO getMovieById(Integer movieId) throws Exception {
        try {
            Map<String,String> additionalData = dataService.fetchAdditionalData(movieId);
            return movieRepository.findByMovieId(movieId).map(movie -> MovieResponseDTO.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .overview(movie.getOverview())
                    .genres(movie.getGenres().stream().map(StringUtils::formatName).collect(Collectors.toSet()))
                    .cast(movie.getCast().stream().map(StringUtils::formatName).collect(Collectors.toSet()))
                    .directors(movie.getCrew().stream().map(StringUtils::formatName).collect(Collectors.toSet()))
                    .recommended_movie_ids(movie.getRecommended_movie_id())
                    .trailer_path(additionalData.get("trailer_path"))
                    .poster_path(additionalData.get("poster_path"))
                    .build()).orElseThrow(() -> new RequiredResourceNotFoundException(
                    "Movie not found!"));
        }catch (Exception e){
            processException(e);
        }
        return null;
    }

}
