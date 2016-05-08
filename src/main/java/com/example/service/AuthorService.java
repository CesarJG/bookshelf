package com.example.service;

import com.example.dao.Author;
import com.example.dao.Book;
import com.example.repository.AuthorRepository;
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
public class AuthorService {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * @param author
     * @return
     */
    public Author saveAuthor(final Author author) {
        return authorRepository.save(author);
    }

    /**
     * @param id
     * @return
     */
    public Author findAuthor(final Long id) {
        return authorRepository.findOne(id);
    }

    /**
     * @return
     */
    public List<Author> findAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }

    /**
     * @param authorId
     * @return
     */
    public List<Book> findBooksOfAuthor(final Long authorId) {
        final Author author = authorRepository.findOne(authorId);
        return new ArrayList<>(author.getBooks());
    }

    /**
     * @param id
     */
    @Transactional
    public void deleteAuthor(final Long id) {

        //First we find the author to delete.
        final Author author = authorRepository.findOne(id);

        //We update the author of the books.
        Set<Book> savedBooks = new HashSet<>();
        for (final Book book : new ArrayList<>(author.getBooks())) {
            book.setAuthor(null);
            savedBooks.add(bookService.saveBook(book));
        }

        //We reassign the persisted books.
        author.setBooks(savedBooks);

        // We save the author to update the references.
        authorRepository.save(author);

        //Once we have saved the books we can safely delete the author.
        authorRepository.delete(author);
    }
}
