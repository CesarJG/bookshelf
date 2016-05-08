package com.example.service;

import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.repository.BookRepository;
import com.example.repository.BookShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJuarez.
 */
@Service
public class BookShelfService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookShelfRepository bookShelfRepository;

    private static final String SEARCH_KEY_TITLE = "title";
    private static final String SEARCH_KEY_LAST_NAME = "lastName";
    private static final String SORT_KEY_ASC = "asc";
    private static final String SORT_KEY_DESC = "desc";

    /**
     * @param bookShelf
     * @return
     */
    public BookShelf saveBookShelf(final BookShelf bookShelf) {
        return bookShelfRepository.save(bookShelf);
    }

    /**
     * @param shelfId
     * @param book
     * @return
     */
    @Transactional
    public BookShelf saveBookIntoShelf(final Long shelfId, final Book book) {
        final BookShelf bookShelf = bookShelfRepository.findOne(shelfId);
        bookShelf.addBook(book);
        return bookShelfRepository.save(bookShelf);
    }

    /**
     * @param id
     * @return
     */
    public BookShelf findBookShelf(final Long id) {
        return bookShelfRepository.findOne(id);
    }

    /**
     * @return
     */
    public List<BookShelf> findAllBookShelves() {
        return (List<BookShelf>) bookShelfRepository.findAll();
    }

    /**
     * @param shelfId
     * @param sortBy
     * @param sort
     * @return
     */
    public List<Book> getBooksOfBookShelf(final Long shelfId, final String sortBy, final String sort) {
        final BookShelf bookShelf = bookShelfRepository.findOne(shelfId);

        List<Book> books = new ArrayList<Book>(bookShelf.getBooks());

        if (sortBy != null) {
            switch (sortBy) {
                case SEARCH_KEY_TITLE:
                    if (sort.equals(SORT_KEY_ASC)) {
                        books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
                    } else if (sort.equals(SORT_KEY_DESC)) {
                        books.sort((b1, b2) -> b2.getTitle().compareToIgnoreCase(b1.getTitle()));
                    }
                    break;

                case SEARCH_KEY_LAST_NAME:
                    if (sort.equals(SORT_KEY_ASC)) {
                        books.sort((b1, b2) -> b1.getAuthor().getLastName().compareToIgnoreCase(b2.getAuthor().getLastName()));
                    } else if (sort.equals(SORT_KEY_DESC)) {
                        books.sort((b1, b2) -> b2.getAuthor().getLastName().compareToIgnoreCase(b1.getAuthor().getLastName()));
                    }
                    break;

                default:
                    return books;
            }
        }

        return books;
    }

    /**
     * @param id
     */
    @Transactional
    public void deleteBookShelf(final Long id) {
        // First we find the book shelf to delete.
        final BookShelf bookShelf = bookShelfRepository.findOne(id);

        // In order to delete a book shelf we have to delete all the books of a book shelf.
        bookShelf.removeAllBooks();

        // We save the book shelf to make the BookShelf --> Book cascade to work.
        bookShelfRepository.save(bookShelf);

        // We can now delete the book shelf.
        bookShelfRepository.delete(id);
    }

    /**
     * @param shelfId
     * @param bookId
     */
    @Transactional
    public void deleteBookFromBookShelf(final Long shelfId, final Long bookId) {
        // First we find the book shelf to delete.
        final BookShelf bookShelf = bookShelfRepository.findOne(shelfId);

        //We also find the book to delete.
        final Book book = bookRepository.findOne(bookId);

        // We remove the book from the shelf.
        bookShelf.removeBook(book);

        // We save the shelf.
        bookShelfRepository.save(bookShelf);
    }
}
