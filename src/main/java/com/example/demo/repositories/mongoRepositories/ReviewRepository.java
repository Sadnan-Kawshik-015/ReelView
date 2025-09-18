package com.example.demo.repositories.mongoRepositories;

import com.example.demo.entities.mongoEntities.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Reviews, String> {
    List<Reviews> findAllByAuthorId(String authorId);
}
