package com.example.demo.controller;

import com.example.demo.dtos.input.MoviePagingDTO;
import com.example.demo.dtos.input.MovieSearchDTO;
import com.example.demo.dtos.output.*;
import com.example.demo.service.MovieService;
import com.example.demo.utils.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
@Tag(name = "Movie api")
@Validated
@RequiredArgsConstructor
public class MovieController extends BaseController{
    private final MovieService movieService;


    @Tags(value = @Tag(name = "RV011"))
    @PostMapping("/movies/search")
    public ResponseEntity<ResponseModelDTO> getAllMovies(@Valid @RequestBody MoviePagingDTO pagingDTO) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            MoviesPaginationResponseDTO projectPagingAndListDTO = movieService.getMyMoviesWithPagination(
                    pagingDTO);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage(projectPagingAndListDTO.getLists().isEmpty()?"No movies found":"Movies found successfully!");
            responseModelDTO.setData(projectPagingAndListDTO);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

    @Tags(value = @Tag(name = "RV012"))
    @GetMapping("/movies/{movie_id}")
    public ResponseEntity<ResponseModelDTO> getMovieById(@PathVariable("movie_id") Integer movieId) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            MovieResponseDTO responseDTO = movieService.getMovieById(
                    movieId);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage(responseDTO==null?"No movies found":"Movies found successfully!");
            responseModelDTO.setData(responseDTO);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV018"))
    @GetMapping("/movies/popular/{n}")
    public ResponseEntity<ResponseModelDTO> getPopularMovies(@PathVariable("n") Integer n) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            List<TopNMovieResponseDTO> responseDTO = movieService.getPopularMovies(
                    n);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage(responseDTO.isEmpty()?"No movies found":"Movies found successfully!");
            responseModelDTO.setData(responseDTO);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

    @Tags(value = @Tag(name = "RV019"))
    @GetMapping("/stats/movies")
    public ResponseEntity<ResponseModelDTO> getStats() {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            MovieStatDTO responseDTO = movieService.getMovieStats();
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage(responseDTO==null?"No data found":"Movies stat found successfully!");
            responseModelDTO.setData(responseDTO);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

}
