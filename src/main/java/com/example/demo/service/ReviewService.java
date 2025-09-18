package com.example.demo.service;

import com.example.demo.dtos.input.CreateReviewDTO;
import com.example.demo.dtos.input.UpdateReviewDTO;
import com.example.demo.dtos.output.ReviewResponseDTO;
import com.example.demo.entities.User;
import com.example.demo.entities.mongoEntities.Movies;
import com.example.demo.entities.mongoEntities.Reviews;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.mongoRepositories.ReviewRepository;
import com.example.demo.service.config.ContextService;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService extends BaseService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;


    @Transactional(rollbackFor = Exception.class)
    public void createReview(CreateReviewDTO inputDTO) throws Exception {
        try {
            String userId = contextService.getCurrentUserIdFromRequestContext();
            User user = userRepository.findById(userId).orElseThrow(() -> new RequiredResourceNotFoundException("User not found"));
            Reviews review = new Reviews();
            review.setReviewBody(inputDTO.getReviewBody());
            review.setAuthor(user.getFullName());
            review.setEmail(user.getEmail());
            review.setCreatedAt(LocalDate.now());
            review.setAuthorId(userId);
            reviewRepository.save(review);
            mongoTemplate.update(Movies.class)
                    .matching(Criteria.where("id").is(inputDTO.getMovieId()))
                    .apply(new Update().push("reviewIds").value(review))
                    .first();
        }catch (Exception e){
            processException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateReview(UpdateReviewDTO inputDTO) throws Exception {
        try {
            String userId = contextService.getCurrentUserIdFromRequestContext();
            Reviews review = reviewRepository.findById(inputDTO.getReviewId()).orElseThrow(() -> new RequiredResourceNotFoundException("Review not found"));
            if(!review.getAuthorId().equals(userId)){
                throw new RequiredResourceNotFoundException("You are not authorized to update this review");
            }
            review.setReviewBody(inputDTO.getReviewBody());
            reviewRepository.save(review);
        }catch (Exception e){
            processException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReview(String reviewId) throws Exception {
        try {
            String userId = contextService.getCurrentUserIdFromRequestContext();
            Reviews review = reviewRepository.findById(reviewId).orElseThrow(() -> new RequiredResourceNotFoundException("Review not found"));
            if(!review.getAuthorId().equals(userId)){
                throw new RequiredResourceNotFoundException("You are not authorized to delete this review");
            }
            reviewRepository.deleteById(reviewId);
        }catch (Exception e){
            processException(e);
        }
    }

    public List<ReviewResponseDTO> getUserReviews() throws Exception{
        try {
            String userId = contextService.getCurrentUserIdFromRequestContext();
            return reviewRepository.findAllByAuthorId(userId)
                    .stream()
                    .map(this::getReviewResponseDTO)
                    .toList();

        }catch (Exception e){
            processException(e);
        }
        return new ArrayList<>();
    }
    public ReviewResponseDTO getReviewById(String reviewId) throws Exception{
        try {
            Reviews review = reviewRepository.findById(reviewId).orElseThrow(() -> new RequiredResourceNotFoundException("Review not found"));
            return getReviewResponseDTO(review);

        }catch (Exception e){
            processException(e);
        }
        return null;
    }

    private ReviewResponseDTO getReviewResponseDTO(Reviews review){
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setReviewId(review.getId().toString());
        dto.setReviewBody(review.getReviewBody());
        dto.setAuthor(review.getAuthor());
        dto.setEmail(review.getEmail());
        dto.setAuthorId(review.getAuthorId());
        dto.setCreatedAt(review.getCreatedAt().toString());
        return dto;
    }



}
