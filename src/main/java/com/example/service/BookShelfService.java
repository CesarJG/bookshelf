package com.example.service;

import com.example.dao.BookShelf;
import com.example.repository.BookRepository;
import com.example.repository.BookShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by CJuarez.
 */
@Service
public class BookShelfService
{

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookShelfRepository bookShelfRepository;

	/**
	 *
	 * @param bookShelf
	 * @return
	 */
	public BookShelf saveBookShelf(final BookShelf bookShelf)
	{
		return bookShelfRepository.save(bookShelf);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public BookShelf findBookShelf(final Long id)
	{
		return bookShelfRepository.findOne(id);
	}

	/**
	 *
	 * @return
	 */
	public List<BookShelf> findAllBookShelves()
	{
		return (List<BookShelf>) bookShelfRepository.findAll();
	}

	/**
	 *
	 * @param id
	 */
	@Transactional
	public void deleteBookShelf(final Long id)
	{
		// First we find the book shelf to delete.
		final BookShelf bookShelf = bookShelfRepository.findOne(id);

		// In order to delete a book shelf we have to delete all the books of a book shelf.
		bookShelf.removeAllBooks();

		// We save the book shelf to make the BookShelf --> Book cascade to work.
		bookShelfRepository.save(bookShelf);

		// We can now delete the book shelf.
		bookShelfRepository.delete(id);
	}
}
