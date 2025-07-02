package com.lingosphinx.profile.repository;

import com.lingosphinx.profile.domain.Subscription;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SubscriptionSpecifications {
    public static Specification<Subscription> hasUserId(UUID userId) {
        return (root, query, cb) -> root.join("profile").get("userId").in(userId);
    }
}