package com.example.service;

import com.example.dao.Author;
import com.example.dao.Book;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJuarez.
 */
@Service
public class AuthorService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    /**
     *
     * @param author
     * @return
     */
    public Author saveAuthor(final Author author){
        return authorRepository.save(author);
    }

    /**
     *
     * @param id
     * @return
     */
    public Author findAuthor(final Long id){
        return authorRepository.findOne(id);
    }

    /**
     *
     * @return
     */
    public List<Author> findAllAuthors(){
        return (List<Author>) authorRepository.findAll();
    }

    /**
     *
     * @param id
     */
    @Transactional
    public void deleteAuthor(final Long id){

        //First we find the author to delete.
        final Author author = authorRepository.findOne(id);

        //In order to delete an author we have to clean the book-->author relationships.
        for(final Book book: new ArrayList<>(author.getBooks())){
            book.removeAuthor(author);
            bookRepository.save(book);
        }

        //Once we have saved the books we can safely delete the author.
        authorRepository.delete(author);
    }
}
