package com.lingosphinx.profile.dto;

import com.lingosphinx.profile.domain.Subscription;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {

    private Long id;
    @Builder.Default
    private Subscription.Type type = Subscription.Type.FREE;
}