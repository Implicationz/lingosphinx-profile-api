package com.lingosphinx.profile.exception;

public class StripeWebhookException extends RuntimeException {
    public StripeWebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
