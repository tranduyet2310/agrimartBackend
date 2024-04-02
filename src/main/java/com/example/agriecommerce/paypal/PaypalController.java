package com.example.agriecommerce.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
@Slf4j
public class PaypalController {
    private final PaypalService paypalService;

    @Autowired
    public PaypalController(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    @PostMapping("create")
    public ResponseEntity<String> createPayment() {
        try {
            String cancelUrl = "http://localhost:8080/api/payment/cancel";
            String successUrl = "http://localhost:8080/api/payment/success";

            Payment payment = paypalService.createPayment(
                    1.0,
                    "USD",
                    "paypal",
                    "sale",
                    "description",
                    cancelUrl,
                    successUrl
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(links.getHref());
                }
            }

        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }
        return ResponseEntity.ok("payment/error");
    }

    @GetMapping("success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                return "OK";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }
        return "success";
    }
    @GetMapping("cancel")
    public String paymentCancel(){
        return "Payment has been failed";
    }
}
