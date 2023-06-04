package com.ossovita.accountingservice.controller;

import com.google.gson.JsonSyntaxException;
import com.ossovita.accountingservice.service.ReservationPaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/1.0/accounting")
@Slf4j
public class StripeWebhookController {

    /*
     For local development:
     We need to forward stripe events to our endpoint via command below
     stripe listen --forward-to localhost:8888/api/1.0/accounting/stripe/events
      */

    @Value("${stripe.webhook.secret.key}")
    String endpointSecret;

    ReservationPaymentService reservationPaymentService;

    public StripeWebhookController(ReservationPaymentService reservationPaymentService) {
        this.reservationPaymentService = reservationPaymentService;
    }

    @PostMapping("/stripe/events")
    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

        if (sigHeader == null) {
            return "";
        }

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (JsonSyntaxException e) {
            // Invalid payload
            log.info("Webhook error with Json Syntax");
            return "";
        } catch (SignatureVerificationException e) {
            // Invalid signature
            log.info("Webhook error while validating signature");
            return "";
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        //Handle the event
        switch (event.getType()) {
            case "payment_intent.created": {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                //paymet intent succeed
                log.info("Payment intent created {} " + paymentIntent.getId());
            }
            case "payment_intent.succeeded": {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                // Then define and call a function to handle the event payment_intent.succeeded
                log.info("Payment intent succeeded {} " + paymentIntent.getId());
                break;
            }
            case "charge.succeeded": {
                //TODO: seperate reservation-payment & subscription-payment depends on the metadata
                Charge charge = (Charge) stripeObject;
                //charge succeeded
                reservationPaymentService.processReservationPaymentCharge(charge);
            }
            // ... handle other event types
            default:
                log.warn("Unhandled event type: " + event.getType());
                break;
        }


        return "";
    }
}
