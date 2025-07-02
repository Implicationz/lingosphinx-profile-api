package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.Subscription;
import com.lingosphinx.profile.dto.SubscriptionDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SubscriptionMapper {
    SubscriptionDto toDto(Subscription subscription);
    Subscription toEntity(SubscriptionDto subscriptionDto);

    void updateEntityFromDto(SubscriptionDto subscriptionDto, @MappingTarget  Subscription existingSubscription);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchnEntityFromDto(SubscriptionDto subscriptionDto, @MappingTarget  Subscription existingSubscription);
}