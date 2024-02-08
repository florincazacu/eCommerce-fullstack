package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.service.BookService;
import com.example.springbootlibrary.utils.ExtractJwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

	private static final String SUB = "\"sub\"";
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping("/secure/currentloans/count")
	public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, SUB);

		return bookService.currentLoansCount(userEmail);
	}

	@GetMapping("/secure/ischeckedout/byuser")
	public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, SUB);

		return bookService.checkoutBookByUser(userEmail, bookId);
	}

	@PutMapping("/secure/checkout")
	public Book checkoutBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, SUB);

		return bookService.checkoutBook(userEmail, bookId);
	}
}
