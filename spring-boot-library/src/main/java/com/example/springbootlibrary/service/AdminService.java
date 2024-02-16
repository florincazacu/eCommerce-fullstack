package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.BookRepository;
import com.example.springbootlibrary.dao.CheckoutRepository;
import com.example.springbootlibrary.dao.ReviewRepository;
import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.requestmodels.AddBookRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService {

	private final BookRepository bookRepository;

	private final ReviewRepository reviewRepository;

	private final CheckoutRepository checkoutRepository;

	public AdminService(BookRepository bookRepository, ReviewRepository reviewRepository, CheckoutRepository checkoutRepository) {
		this.bookRepository = bookRepository;
		this.reviewRepository = reviewRepository;
		this.checkoutRepository = checkoutRepository;
	}

	public void increaseBookQuantity(Long bookId) throws Exception {
		Optional<Book> optionalBook = bookRepository.findById(bookId);

		if (optionalBook.isEmpty()) {
			throw new Exception("Book not found");
		}

		Book book = optionalBook.get();
		book.setCopiesAvailable(book.getCopiesAvailable() + 1);
		book.setCopies(book.getCopies() + 1);

		bookRepository.save(book);
	}

	public void decreaseBookQuantity(Long bookId) throws Exception {
		Optional<Book> optionalBook = bookRepository.findById(bookId);

		if (optionalBook.isEmpty()) {
			throw new Exception("Book not found");
		}

		Book book = optionalBook.get();

		if (book.getCopiesAvailable() <= 0 || book.getCopies() <= 0) {
			throw new Exception("Quantity locked");
		}

		book.setCopiesAvailable(book.getCopiesAvailable() - 1);
		book.setCopies(book.getCopies() - 1);

		bookRepository.save(book);
	}

	public void postBook(AddBookRequest addBookRequest) {
		Book book = new Book();

		book.setTitle(addBookRequest.getTitle());
		book.setAuthor(addBookRequest.getAuthor());
		book.setDescription(addBookRequest.getDescription());
		book.setCopies(addBookRequest.getCopies());
		book.setCopiesAvailable(addBookRequest.getCopies());
		book.setCategory(addBookRequest.getCategory());
		book.setImage(addBookRequest.getImage());

		bookRepository.save(book);
	}

	public void deleteBook(Long bookId) throws Exception {
		Optional<Book> bookOptional = bookRepository.findById(bookId);

		if (bookOptional.isEmpty()) {
			throw new Exception("Book not found");
		}

		bookRepository.delete(bookOptional.get());
		checkoutRepository.deleteAllByBookId(bookId);
		reviewRepository.deleteAllByBookId(bookId);
	}
}
