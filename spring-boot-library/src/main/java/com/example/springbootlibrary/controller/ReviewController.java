package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.requestmodels.ReviewRequest;
import com.example.springbootlibrary.service.ReviewService;
import com.example.springbootlibrary.utils.ExtractJwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@GetMapping("/secure/user/book")
	public Boolean reviewBookByUser(@RequestHeader(value="Authorization") String token,
	                                @RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		if (userEmail == null) {
			throw new Exception("User email is missing");
		}

		return reviewService.userReviewListed(userEmail, bookId);
	}

	@PostMapping("/secure")
	public void postReview(@RequestHeader(value="Authorization") String token,
	                       @RequestBody ReviewRequest reviewRequest) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		if (userEmail == null) {
			throw new Exception("User email is missing");
		}

		reviewService.postReview(userEmail, reviewRequest);
	}
}
