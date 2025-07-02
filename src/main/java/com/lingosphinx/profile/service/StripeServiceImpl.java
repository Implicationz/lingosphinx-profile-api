package com.lingosphinx.profile.service;

import com.lingosphinx.profile.domain.Subscription;
import com.lingosphinx.profile.dto.SubscriptionDto;
import com.lingosphinx.profile.exception.StripeServiceException;
import com.lingosphinx.profile.exception.StripeWebhookException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;


    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final SubscriptionService subscriptionService;


    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public String createCheckoutSession(Long userId, String successUrl, String cancelUrl) {
        try {
            var params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPrice("stripe_price_id")
                                    .build()
                    )
                    .putMetadata("userId", userId.toString())
                    .build();

            var session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new StripeServiceException("Error creating Stripe Checkout Session", e);
        }
    }

    @Override
    public void handleStripeWebhook(String sigHeader, String payload) {
        try {
            var event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            switch (event.getType()) {
                case "checkout.session.completed" -> {
                    var session = (Session) event.getDataObjectDeserializer().getObject().get();
                    var userId = session.getMetadata().get("userId");
                }
                case "customer.subscription.created" -> {
                    handleSubscriptionEvent(event);
                }
                case "customer.subscription.updated" -> {
                    handleSubscriptionEvent(event);
                }
                case "customer.subscription.deleted" -> {
                    handleSubscriptionEvent(event);
                }
                case "invoice.payment_succeeded" -> {
                    var invoice = event.getDataObjectDeserializer().getObject().get();
                    var userId = extractUserIdFromInvoice(invoice);
                    subscriptionService.markPaymentSucceeded(userId);
                }
                case "invoice.payment_failed" -> {
                    var invoice = event.getDataObjectDeserializer().getObject().get();
                    var userId = extractUserIdFromInvoice(invoice);
                    subscriptionService.markPaymentFailed(userId);
                }
                default -> {
                }
            }
        } catch (SignatureVerificationException e) {
            throw new StripeWebhookException("Invalid Stripe Webhook-Signature", e);
        }
    }

    protected void handleSubscriptionEvent(Event event) {
        var object = event.getDataObjectDeserializer().getObject().get();
        var stripeSubscription = (com.stripe.model.Subscription) object;
        var stripeId = stripeSubscription.getId();
        var metadata = stripeSubscription.getMetadata();
        var userId = metadata.get("userId");
        var type = "active".equals(stripeSubscription.getStatus()) ? Subscription.Type.PREMIUM : Subscription.Type.FREE;

        subscriptionService.patchByUserId(UUID.fromString(userId), (subscription) -> {
            subscription.setType(type);
            subscription.setStripeId(stripeId);
        });
    }

    protected String extractUserIdFromInvoice(Object invoiceObj) {
        var invoice = (Invoice) invoiceObj;
        var metadata = invoice.getMetadata();
        return metadata != null ? metadata.get("userId") : null;
    }
}