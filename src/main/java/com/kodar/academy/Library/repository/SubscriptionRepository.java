package com.kodar.academy.Library.repository;

import com.kodar.academy.Library.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
