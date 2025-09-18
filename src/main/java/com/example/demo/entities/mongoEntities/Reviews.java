package com.example.demo.entities.mongoEntities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "reviews")
public class Reviews {
    @Id
    private ObjectId id;
    private String reviewBody;
    private String authorId;
    private String author;
    private String email;
    private LocalDate createdAt;

}
