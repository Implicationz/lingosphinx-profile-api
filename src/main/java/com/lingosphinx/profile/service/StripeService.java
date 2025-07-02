package com.lingosphinx.profile.service;

import com.lingosphinx.profile.dto.SubscriptionDto;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;

import java.util.List;

public interface StripeService {
    String createCheckoutSession(Long userId, String successUrl, String cancelUrl);

    void handleStripeWebhook(String sigHeader, String payload);
}

