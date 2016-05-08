package com.example.config;

import com.example.dao.Author;
import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.repository.BookShelfRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by CJuarez.
 */
@Service
public class PreLoadData implements InitializingBean {

    @Autowired
    private BookShelfRepository bookShelfRepository;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {

        final BookShelf myShelf = new BookShelf();

        final Author shakespeare = new Author("William", "Shakespeare");
        final Author cervantes = new Author("Miguel", "de Cervantes");

        final Book hamlet = new Book("Hamlet", "HamletISBN");
        hamlet.setAuthor(shakespeare);

        final Book quijote = new Book("Quijote", "QuijoteISBN");
        quijote.setAuthor(cervantes);

        myShelf.addBook(hamlet);
        myShelf.addBook(quijote);

        bookShelfRepository.save(myShelf);
    }
}