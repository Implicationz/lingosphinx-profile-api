package com.lingosphinx.profile.service;


import com.lingosphinx.profile.domain.Subscription;
import com.lingosphinx.profile.dto.SubscriptionDto;
import com.lingosphinx.profile.mapper.SubscriptionMapper;
import com.lingosphinx.profile.repository.SubscriptionRepository;
import com.lingosphinx.profile.repository.SubscriptionSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionDto create(SubscriptionDto subscriptionDto) {
        var subscription = subscriptionMapper.toEntity(subscriptionDto);
        var savedSubscription = subscriptionRepository.save(subscription);
        log.info("Subscription created with id: {}", savedSubscription.getId());
        return subscriptionMapper.toDto(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionDto readById(Long id) {
        log.info("Reading subscription by id: {}", id);
        var subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Subscription not found for id: {}", id);
                    return new EntityNotFoundException("Subscription not found");
                });
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDto> readAll() {
        log.info("Reading all subscriptions");
        return subscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }

    @Override
    public SubscriptionDto update(Long id, SubscriptionDto subscriptionDto) {
        log.info("Updating subscription with id: {}", id);
        var existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Subscription not found for update, id: {}", id);
                    return new EntityNotFoundException("Subscription not found");
                });
        subscriptionMapper.updateEntityFromDto(subscriptionDto, existingSubscription);
        var savedSubscription = subscriptionRepository.save(existingSubscription);
        log.info("Subscription updated with id: {}", savedSubscription.getId());
        return subscriptionMapper.toDto(savedSubscription);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting subscription with id: {}", id);
        subscriptionRepository.deleteById(id);
        log.info("Subscription deleted with id: {}", id);
    }

    @Override
    public SubscriptionDto patchByUserId(UUID userId, Consumer<Subscription> patcher) {
        log.info("Patching subscription type for userId: {}", userId);
        var found = subscriptionRepository.findOne(SubscriptionSpecifications.hasUserId(userId))
                .orElseThrow(() -> {
                    log.warn("Subscription not found for userId: {}", userId);
                    return new EntityNotFoundException("Subscription not found");
                });
        patcher.accept(found);
        log.info("Subscription patched for userId: {}", userId);
        return subscriptionMapper.toDto(found);
    }

    @Override
    public void markPaymentSucceeded(String userId) {
        log.info("Marking payment as succeeded for userId: {}", userId);
    }

    @Override
    public void markPaymentFailed(String userId) {
        log.info("Marking payment as failed for userId: {}", userId);
    }
}