package com.ossovita.reservationservice.business.concretes;

import com.ossovita.commonservice.core.dataAccess.ReviewRepository;
import com.ossovita.commonservice.core.entities.Review;
import com.ossovita.commonservice.core.entities.dtos.ReviewSaveFormDto;
import com.ossovita.reservationservice.business.abstracts.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReviewManager implements ReviewService {

    ModelMapper modelMapper;
    ReviewRepository reviewRepository;

    public ReviewManager(ModelMapper modelMapper, ReviewRepository reviewRepository) {
        this.modelMapper = modelMapper;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(ReviewSaveFormDto reviewSaveFormDto) {
        Review review = modelMapper.map(reviewSaveFormDto, Review.class);
        return reviewRepository.save(review);
    }
}
