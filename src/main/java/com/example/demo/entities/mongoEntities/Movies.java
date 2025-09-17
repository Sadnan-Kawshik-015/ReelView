package com.example.demo.entities.mongoEntities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "movies")
public class Movies {
    @Id
    private String id;
    @Field("id")
    private Integer movie_id;
    private String title;
    private Set<String> genres;
    private Set<String> keywords;
    private String overview;
    private Set<String> cast;
    private Set<String> crew;
    private Double popularity;
    private String tags;
    private String poster_path;
    private Set<String> recommended_movie_id;
}
