package com.ossovita.reservationservice.controllers;

import com.ossovita.commonservice.core.entities.Review;
import com.ossovita.commonservice.core.entities.dtos.ReviewSaveFormDto;
import com.ossovita.reservationservice.business.abstracts.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/reservation/reviews")
public class ReviewController {

    ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create-review")
    public Review createReview(@RequestBody ReviewSaveFormDto reviewSaveFormDto){
        return reviewService.createReview(reviewSaveFormDto);
    }
}


