package com.example.demo.controller;

import com.example.demo.dtos.input.CreateReviewDTO;
import com.example.demo.dtos.input.UpdateReviewDTO;
import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.dtos.output.ReviewResponseDTO;
import com.example.demo.service.ReviewService;
import com.example.demo.utils.enums.Status;
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
@Tag(name = "Review api")
@Validated
@RequiredArgsConstructor
public class ReviewController extends BaseController{
    private final ReviewService reviewService;

    @Tags(value = @Tag(name = "RV013"))
    @PostMapping("/reviews")
    public ResponseEntity<ResponseModelDTO> createReview(@Valid @RequestBody CreateReviewDTO createReviewDTO) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            reviewService.createReview(createReviewDTO);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage("Review created successfully!");
            responseModelDTO.setData(null);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV014"))
    @PutMapping("/reviews")
    public ResponseEntity<ResponseModelDTO> updateReview(@Valid @RequestBody UpdateReviewDTO updateReviewDTO) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            reviewService.updateReview(updateReviewDTO);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage("Review updated successfully!");
            responseModelDTO.setData(null);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

    @Tags(value = @Tag(name = "RV015"))
    @GetMapping("/reviews/{review_id}")
    public ResponseEntity<ResponseModelDTO> getReviewById(@PathVariable("review_id") String reviewId) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            ReviewResponseDTO responseDTO = reviewService.getReviewById(
                    reviewId);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage("Review found successfully!");
            responseModelDTO.setData(responseDTO);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV016"))
    @GetMapping("/reviews")
    public ResponseEntity<ResponseModelDTO> getUserReviews() {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            List<ReviewResponseDTO> responseDTOs = reviewService.getUserReviews();
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage("Review found successfully!");
            responseModelDTO.setData(responseDTOs);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV017"))
    @DeleteMapping("/reviews/{review_id}")
    public ResponseEntity<ResponseModelDTO> deleteUserReview(@PathVariable("review_id") String reviewId) {
        try {
            ResponseModelDTO responseModelDTO = ResponseModelDTO.builder()
                    .build();
            reviewService.deleteReview(reviewId);
            responseModelDTO.setStatus(Status.SUCCESS.getValue());
            responseModelDTO.setMessage("Review deleted successfully!");
            responseModelDTO.setData(null);
            return ResponseEntity.ok(responseModelDTO);
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
}
