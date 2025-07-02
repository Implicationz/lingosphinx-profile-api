package com.lingosphinx.profile.controller;

import com.lingosphinx.profile.service.StripeService;
import com.lingosphinx.profile.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestHeader("Stripe-Signature") String sigHeader,
                                                      @RequestBody String payload) {
        stripeService.handleStripeWebhook(sigHeader, payload);
        return ResponseEntity.ok("");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> createCheckoutSession(@RequestParam Long userId,
                                                        @RequestParam String successUrl,
                                                        @RequestParam String cancelUrl) {
        var checkoutUrl = stripeService.createCheckoutSession(userId, successUrl, cancelUrl);
        return ResponseEntity.ok(checkoutUrl);
    }
}