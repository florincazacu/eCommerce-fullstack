package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.PaymentRepository;
import com.example.springbootlibrary.entity.Payment;
import com.example.springbootlibrary.requestmodels.PaymentInfoRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.key.secret}") String stripeSecretKey) {
		this.paymentRepository = paymentRepository;
		Stripe.apiKey = stripeSecretKey;
	}

	public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
		List<String> paymentMethodTypes = List.of("card");

		Map<String, Object> params = new HashMap<>();

		params.put("amount", paymentInfoRequest.getAmount());
		params.put("currency", paymentInfoRequest.getCurrency());
		params.put("payment_method_types", paymentMethodTypes);

		return PaymentIntent.create(params);
	}

	public ResponseEntity<String> stripePayment(String userEmail) throws Exception {
		Payment payment = paymentRepository.findByUserEmail(userEmail);

		if (payment == null) {
			throw new Exception("Payment information is missing");
		}

		payment.setAmount(0.00);
		paymentRepository.save(payment);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
