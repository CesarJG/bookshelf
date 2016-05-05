package com.example.service;

import com.example.dao.Book;
import com.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by CJuarez.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     *
     * @param book
     * @return
     */
    public Book saveBook(final Book book){
        return bookRepository.save(book);
    }

    /**
     *
     * @param id
     * @return
     */
    public Book findBook(final Long id){
        return bookRepository.findOne(id);
    }

    /**
     *
     * @return
     */
    public List<Book> findAllBooks(){
        return (List<Book>) bookRepository.findAll();
    }

    /**
     *
     * @param id
     */
    @Transactional
    public void deleteBook(final Long id){

        //First we find the book to delete.
        final Book book = bookRepository.findOne(id);

        //In order to delete a book we have to clean the book-->author relationships.
        book.removeAllAuthors();

        //We save the book to make the cascade take effect in the author side.
        bookRepository.save(book);

        //We can now delete the book (All the process was transactional).
        bookRepository.delete(id);
    }
}
