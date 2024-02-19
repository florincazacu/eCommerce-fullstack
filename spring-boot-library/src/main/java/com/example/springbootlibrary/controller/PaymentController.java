package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.requestmodels.PaymentInfoRequest;
import com.example.springbootlibrary.service.PaymentService;
import com.example.springbootlibrary.utils.ExtractJwt;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/payment-intent")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws StripeException {
		PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
		String paymentString = paymentIntent.toJson();

		return new ResponseEntity<>(paymentString, HttpStatus.OK);
	}

	@PutMapping("/payment-complete")
	public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value = "Authorization") String token) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		if (userEmail == null) {
			throw new Exception("User email is missing");
		}

		return paymentService.stripePayment(userEmail);
	}
}
