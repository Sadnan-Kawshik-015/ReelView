package com.example.demo.service;

import com.example.demo.dtos.external.TMDBResponseDTO;
import com.example.demo.dtos.external.TMDBVideoDTO;
import com.example.demo.dtos.external.TMDBVideoResultsDTO;
import com.example.demo.dtos.input.MoviePagingDTO;
import com.example.demo.dtos.output.MovieResponseDTO;
import com.example.demo.dtos.output.MovieStatDTO;
import com.example.demo.dtos.output.MoviesPaginationResponseDTO;
import com.example.demo.dtos.output.TopNMovieResponseDTO;
import com.example.demo.entities.mongoEntities.Movies;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.mongoRepositories.MovieRepository;
import com.example.demo.repositories.mongoRepositories.ReviewRepository;
import com.example.demo.utils.StringUtils;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService extends BaseService {
    private final MovieRepository movieRepository;
    private final TMDBDataService dataService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

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

    public List<TopNMovieResponseDTO> getPopularMovies(int n) throws Exception {
        try {
            List<Movies> movies = movieRepository.findByOrderByPopularityDesc(PageRequest.of(0, n));
            List<Integer> movieIds = movies.stream().map(Movies::getMovie_id).toList();
            Map<Integer,Map<String,String>> additionalDataMap = new HashMap<>();
            for (Integer movieId : movieIds) {
                additionalDataMap.put(movieId, dataService.fetchAdditionalData(movieId));
            }
            return movies.stream().map(movie -> TopNMovieResponseDTO.builder()
                    .id(movie.getId())
                    .title(movie.getTitle())
                    .overview(movie.getOverview())
                    .popularity(movie.getPopularity())
                    .genres(movie.getGenres().stream().map(StringUtils::formatName).collect(Collectors.toSet()))
                    .poster_path(additionalDataMap.get(movie.getMovie_id()).get("poster_path"))
                    .trailer_path(additionalDataMap.get(movie.getMovie_id()).get("trailer_path"))
                    .backdrop_path(additionalDataMap.get(movie.getMovie_id()).get("backdrop_path"))
                    .build()).collect(Collectors.toList());
        }catch (Exception e){
            processException(e);
        }
        return new ArrayList<>();
    }
    public MovieStatDTO getMovieStats() throws Exception {
        try {
            List<Movies> movies = movieRepository.findAll();
            Integer totalMovies = Math.toIntExact(movies.size());
            Integer totalGenres = movies.stream().flatMap(movie -> movie.getGenres().stream()).collect(Collectors.toSet()).size();
            Integer totalRevies = Math.toIntExact(reviewRepository.count());
            Integer totalUsers = Math.toIntExact(userRepository.count());
            return MovieStatDTO.builder()
                    .totalMovies(totalMovies)
                    .totalUniqueGenres(totalGenres)
                    .totalReviews(totalRevies)
                    .totalUsers(totalUsers)
                    .build();

        }catch (Exception e){
            processException(e);
        }
        return null;
    }

}
