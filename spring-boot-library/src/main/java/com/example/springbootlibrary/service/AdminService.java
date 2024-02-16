package com.example.springbootlibrary.service;

import com.example.springbootlibrary.dao.BookRepository;
import com.example.springbootlibrary.entity.Book;
import com.example.springbootlibrary.requestmodels.AddBookRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

	private final BookRepository bookRepository;

	public AdminService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
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
}
