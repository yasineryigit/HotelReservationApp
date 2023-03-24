package com.ossovita.reservationservice.business.abstracts;

import com.ossovita.commonservice.core.entities.Review;
import com.ossovita.commonservice.core.entities.dtos.ReviewSaveFormDto;

public interface ReviewService {
    Review createReview(ReviewSaveFormDto reviewSaveFormDto);
}
