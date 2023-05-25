package com.ossovita.userservice.repository;

import com.ossovita.userservice.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    boolean existsBySubscriptionPlanPk(long subscriptionPlanPk);
}
