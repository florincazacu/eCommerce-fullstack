package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.BookRepository;
import com.example.springbootlibrary.dao.CheckoutRepository;
import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.entity.Checkout;
import com.example.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

	private final BookRepository bookRepository;

	private final CheckoutRepository checkoutRepository;

	public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository) {
		this.bookRepository = bookRepository;
		this.checkoutRepository = checkoutRepository;
	}

	public Book checkoutBook(String userEmail, Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

		if (book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
			throw new Exception("Book doesn't exist or is already checked out by the user");
		}

		book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);

		bookRepository.save(book.get());

		Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(),
			LocalDate.now().plusDays(7).toString(), book.get().getId());

		checkoutRepository.save(checkout);

		return book.get();
	}

	public Boolean checkoutBookByUser(String userEmail, Long bookId) {
		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

		return validateCheckout != null;
	}

	public int currentLoansCount(String userEmail) {
		return checkoutRepository.findBooksByUserEmail(userEmail).size();
	}

	public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
		List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

		List<Checkout> checkouts = checkoutRepository.findBooksByUserEmail(userEmail);
		List<Long> bookIds = new ArrayList<>();

		for (Checkout checkout : checkouts) {
			bookIds.add(checkout.getBookId());
		}

		List<Book> books = bookRepository.findBooksByBookIds(bookIds);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (Book book : books) {
			Optional<Checkout> checkout = checkouts.stream()
				.filter(e -> e.getBookId().equals(book.getId()))
				.findFirst();

			if (checkout.isPresent()) {
				Date returnDate = simpleDateFormat.parse(checkout.get().getReturnDate());
				Date currentDate = simpleDateFormat.parse(LocalDate.now().toString());

				TimeUnit time = TimeUnit.DAYS;

				long differenceInTime = time.convert(returnDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);

				shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) differenceInTime));
			}
		}

		return shelfCurrentLoansResponses;
	}

	public void returnBook(String userEmail, Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

		if (book.isEmpty() || validateCheckout == null) {
			throw new Exception("Book does not exist or not checked out by user");
		}

		book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
		bookRepository.save(book.get());
		checkoutRepository.deleteById(validateCheckout.getId());
	}

	public void renewLoan(String userEmail, Long bookId) throws Exception {
		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

		if (validateCheckout == null) {
			throw new Exception("Book does not exist or not checked out by user");
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date returnDate = simpleDateFormat.parse(validateCheckout.getReturnDate());
		Date currentDate = simpleDateFormat.parse(LocalDate.now().toString());

		if (returnDate.compareTo(currentDate) > 0 || returnDate.compareTo(currentDate) == 0) {
			validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
			checkoutRepository.save(validateCheckout);
		}
	}
}
