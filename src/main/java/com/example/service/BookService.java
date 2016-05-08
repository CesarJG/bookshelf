package com.example.service;

import com.example.dao.Author;
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
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookShelfService bookShelfService;

    @Autowired
    private AuthorService authorService;

    private static final String GET_BY_TITLE = "title";
    private static final String GET_BY_ISBN = "isbn";
    private static final String GET_BY_LAST_NAME_AUTHOR = "lastName";

    /**
     * @param book
     * @return
     */
    public Book saveBook(final Book book) {
        return bookRepository.save(book);
    }

    /**
     * @param id
     * @return
     */
    public Book findBook(final Long id) {
        return bookRepository.findOne(id);
    }

    /**
     * @param getBy
     * @return
     */
    public List<Book> getBooksBy(final String getBy, final String value) {
        List<Book> books = null;
        switch (getBy) {
            case GET_BY_TITLE:
                books = bookRepository.findByTitle(value);
                break;
            case GET_BY_ISBN:
                books = bookRepository.findByIsbn(value);
                break;
            case GET_BY_LAST_NAME_AUTHOR:
                books = bookRepository.findBooksByAuthorLastName(value);
                break;
        }
        return books;
    }

    /**
     * @return
     */
    public List<Book> findAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    /**
     * @param id
     */
    @Transactional
    public void deleteBook(final Long id) {
        // First we find the book to delete.
        final Book book = bookRepository.findOne(id);

        //In order to delete a book we have to make the BookShelf --> Book cascade to work.
        Set<BookShelf> savedShelves = new HashSet<>();
        for (final BookShelf bookShelf : new ArrayList<>(book.getShelves())) {
            bookShelf.removeBook(book);
            savedShelves.add(bookShelfService.saveBookShelf(bookShelf));
        }

        //We reassign the persisted shelves.
        book.setShelves(savedShelves);

        // We delete the author and save it.
        final Author author = book.getAuthor();
        author.removeBook(book);
        authorService.saveAuthor(author);

        // We save the book.
        bookRepository.save(book);

        // We can now delete the book.
        bookRepository.delete(id);
    }
}
