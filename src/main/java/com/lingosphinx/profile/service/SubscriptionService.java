package com.lingosphinx.profile.service;

import com.lingosphinx.profile.domain.Subscription;
import com.lingosphinx.profile.dto.SubscriptionDto;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface SubscriptionService {
    SubscriptionDto create(SubscriptionDto subscription);
    SubscriptionDto readById(Long id);
    List<SubscriptionDto> readAll();
    SubscriptionDto update(Long id, SubscriptionDto subscription);
    void delete(Long id);

    SubscriptionDto patchByUserId(UUID userId, Consumer<Subscription> patcher);

    void markPaymentSucceeded(String userId);
    void markPaymentFailed(String userId);
}
