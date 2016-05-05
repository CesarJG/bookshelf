package com.example;

import com.example.dao.Author;
import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookshelfApplication.class)
@WebAppConfiguration
@IntegrationTest
public class BookshelfApplicationTests {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    final RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setup() throws Exception {
        //We create test data.
        final BookShelf myShelf = new BookShelf("cesar");

        final Author shakespeare = new Author("William", "Shakespeare");
        final Author cervantes = new Author("Miguel", "de Cervantes");

        final Book hamlet = new Book("Hamlet", "HamletISBN");
        hamlet.addAuthor(shakespeare);
        hamlet.addBookShelf(myShelf);

        final Book quijote = new Book("Quijote", "QuijoteISBN");
        quijote.addAuthor(cervantes);
        quijote.addBookShelf(myShelf);

        HttpEntity<Book> request = new HttpEntity<>(hamlet);
        restTemplate.postForObject("http://localhost:9010/book/v1", request, Book.class);

        request = new HttpEntity<>(quijote);
        restTemplate.postForObject("http://localhost:9010/book/v1", request, Book.class);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDeleteBook() {
        final Author a1 = new Author("firstName1", "lastName1");
        final Author a2 = new Author("firstName2", "lastName2");

        final Book b1 = new Book("title1", "ISBN1");
        final Book b2 = new Book("title2", "ISBN2");

        b1.addAuthor(a1);
        b1.addAuthor(a2);

        Book bookSaved = bookRepository.save(b1);

        bookSaved.removeAuthor(a1);
        bookSaved.removeAuthor(a2);

        bookSaved = bookRepository.save(bookSaved);
        bookRepository.delete(bookSaved.getId());
        bookSaved = bookRepository.findOne(bookSaved.getId());

        int a = 0;
    }

    @Test
    public void testDeleteAuthor() {
        final Author a1 = new Author("firstName1", "lastName1");
        final Author a2 = new Author("firstName2", "lastName2");

        final Book b1 = new Book("title1", "ISBN1");
        final Book b2 = new Book("title2", "ISBN2");

        b1.addAuthor(a1);
        b1.addAuthor(a2);

        Book bookSaved = bookRepository.save(b1);

        Author authorToDelete = authorRepository.findOne(b1.getId());

        for (final Book book : new ArrayList<>(authorToDelete.getBooks())) {
            book.removeAuthor(authorToDelete);
            bookRepository.save(book);
        }

        authorRepository.delete(authorToDelete);

        authorToDelete = authorRepository.findOne(authorToDelete.getId());

        int a = 0;
    }

    @Test
    public void bookCRUDTest() {

        //We obtain all the books...
        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity("http://localhost:9010/book/v1", Book[].class);
        Book[] objects = responseEntity.getBody();

        //... and we see that we had two.
        assertTrue(objects.length == 2);

        //We the the isbn of the first book and add an author for it...
        objects[0].setIsbn("testISBN");
        objects[0].addAuthor(new Author("firstName", "lastName"));
        HttpEntity<Book> request = new HttpEntity<>(objects[0]);
        restTemplate.postForObject("http://localhost:9010/book/v1", request, Book.class);

        //... and check that it was changed
        ResponseEntity<Book> response = restTemplate.getForEntity("http://localhost:9010/book/v1/" + objects[0].getId(), Book.class);
        assertTrue(response.getBody().getIsbn().equals("testISBN"));
        assertTrue(response.getBody().getAuthors().size() == 2);

        //We now delete that book...
        restTemplate.delete("http://localhost:9010/book/v1/" + objects[0].getId());

        //... and see it was deleted.
        response = restTemplate.getForEntity("http://localhost:9010/book/v1/" + objects[0].getId(), Book.class);
        assertNull(response.getBody());

    }

    @Test
    public void authorCRUDTest() {

        //We obtain all the books...
        ResponseEntity<Author[]> responseEntity = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        Author[] objects = responseEntity.getBody();



    }

    @After
    public void release() {

    }

}
