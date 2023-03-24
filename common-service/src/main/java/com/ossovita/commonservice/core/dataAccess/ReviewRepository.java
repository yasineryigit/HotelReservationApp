package com.ossovita.commonservice.core.dataAccess;

import com.ossovita.commonservice.core.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}

