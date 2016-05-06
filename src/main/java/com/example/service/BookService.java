package com.example.service;

import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CJuarez.
 */
@Service
public class BookService
{

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookShelfService bookShelfService;

	/**
	 *
	 * @param book
	 * @return
	 */
	public Book saveBook(final Book book)
	{
		return bookRepository.save(book);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Book findBook(final Long id)
	{
		return bookRepository.findOne(id);
	}

	/**
	 *
	 * @return
	 */
	public List<Book> findAllBooks()
	{
		return (List<Book>) bookRepository.findAll();
	}

	/**
	 *
	 * @param id
	 */
	@Transactional
	public void deleteBook(final Long id)
	{
		// First we find the book to delete.
		final Book book = bookRepository.findOne(id);

        //In order to delete a book we have to make the BookShelf --> Book cascade to work.
        Set<BookShelf> savedShelves = new HashSet<>();
        for(final BookShelf bookShelf: new ArrayList<>(book.getShelves())){
            bookShelf.removeBook(book);
            savedShelves.add(bookShelfService.saveBookShelf(bookShelf));
        }
        
        //We reassign the persisted shelves.
        book.setShelves(savedShelves);

		// We delete all the authors of a book.
		book.removeAllAuthors();

		// We save the book to make the Book --> Author cascade to work.
		bookRepository.save(book);

		// We can now delete the book.
		bookRepository.delete(id);
	}
}
