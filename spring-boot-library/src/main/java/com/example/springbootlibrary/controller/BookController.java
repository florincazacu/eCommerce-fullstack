package com.example.springbootlibrary.controller;

import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.example.springbootlibrary.service.BookService;
import com.example.springbootlibrary.utils.ExtractJwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping("/secure/currentloans")
	public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		return bookService.currentLoans(userEmail);
	}

	@GetMapping("/secure/currentloans/count")
	public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		return bookService.currentLoansCount(userEmail);
	}

	@GetMapping("/secure/ischeckedout/byuser")
	public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		return bookService.checkoutBookByUser(userEmail, bookId);
	}

	@PutMapping("/secure/checkout")
	public Book checkoutBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		return bookService.checkoutBook(userEmail, bookId);
	}

	@PutMapping("/secure/return")
	public void returnBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);
		bookService.returnBook(userEmail, bookId);
	}

	@PutMapping("/secure/renew/loan")
	public void renewLoan(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJwtExtraction(token, Util.SUB);

		bookService.renewLoan(userEmail, bookId);
	}
}
