package com.lingosphinx.profile.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lingosphinx.profile.service.SubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class StripeControllerTest {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    private String generateHeader(String payload, String secret) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String signedPayload = timestamp + "." + payload;
        Mac sha256Mac = Mac.getInstance("HmacSHA256");
        sha256Mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
        byte[] signature = sha256Mac.doFinal(signedPayload.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : signature) {
            sb.append(String.format("%02x", b));
        }
        return "t=" + timestamp + ",v1=" + sb;
    }

    @Test
    void handleStripeWebhook_subscriptionCreated_updatesType() throws Exception {
        String userId = "123e4567-e89b-12d3-a456-426614174000";


        String payload = """
{
   "id": "evt_1P1zRJ2eZvKYlo2C3XPt8FVS",
   "object": "event",
   "api_version": "2023-10-16",
   "created": 1721443200,
   "data": {
     "object": {
       "id": "sub_1P1zQw2eZvKYlo2Cdy1Q8sPN",
       "object": "subscription",
       "application_fee_percent": null,
       "billing_cycle_anchor": 1721443200,
       "cancel_at": null,
       "cancel_at_period_end": false,
       "canceled_at": null,
       "collection_method": "charge_automatically",
       "created": 1721443200,
       "currency": "usd",
       "current_period_end": 1724035200,
       "current_period_start": 1721443200,
       "customer": "cus_OiYwO9jxZ1KvSd",
       "items": {
         "object": "list",
         "data": [
           {
             "id": "si_OiYwbAzrqK3ctd",
             "object": "subscription_item",
             "price": {
               "id": "price_1P1zQv2eZvKYlo2ClMGlJ2IG",
               "object": "price",
               "active": true,
               "billing_scheme": "per_unit",
               "currency": "usd",
               "interval": "month",
               "product": "prod_Plan12345",
               "unit_amount": 500
             },
             "quantity": 1
           }
         ]
       },
       "latest_invoice": "in_1P1zQy2eZvKYlo2C2RYTzS4Q",
       "livemode": false,
       "metadata": {
           "userId": "%s"
         },
       "status": "active"
     }
   },
   "livemode": false,
   "pending_webhooks": 1,
   "request": {
     "id": "req_abc123",
     "idempotency_key": null
   },
   "type": "customer.subscription.created"
 }              
""".formatted(userId);
        String sigHeader = generateHeader(payload, endpointSecret);

        Mockito.doNothing().when(subscriptionService)
                .patchByUserId(java.util.UUID.fromString(userId), Mockito.any(Consumer.class));

        mockMvc.perform(post("/api/stripe/webhook")
                        .header("Stripe-Signature", sigHeader)
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isOk());

        Mockito.verify(subscriptionService)
                .patchByUserId(java.util.UUID.fromString(userId), Mockito.any(Consumer.class));
    }
}