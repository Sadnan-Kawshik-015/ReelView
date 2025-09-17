package com.example.demo.repositories.mongoRepositories;

import com.example.demo.entities.mongoEntities.Movies;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movies,String> {
    @Query("{}")
    List<Movies> findTop10ByOrderByPopularityDesc(Pageable pageable);
    @Query("{ 'movie_id': ?#{#movieId} }")
    Optional<Movies> findByMovieId(@Param("movieId") Integer movieId);


}
