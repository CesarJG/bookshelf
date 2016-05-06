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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookshelfApplication.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookshelfApplicationTests
{
	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	final RestTemplate restTemplate = new RestTemplate();

	@Before
    public void setup() throws Exception {

        //We create test data.
        final BookShelf myShelf = new BookShelf();

        final Author shakespeare = new Author("William", "Shakespeare");
        final Author cervantes = new Author("Miguel", "de Cervantes");

        final Book hamlet = new Book("Hamlet", "HamletISBN");
        hamlet.addAuthor(shakespeare);

        final Book quijote = new Book("Quijote", "QuijoteISBN");
        quijote.addAuthor(cervantes);

        myShelf.addBook(hamlet);
        myShelf.addBook(quijote);

        HttpEntity<BookShelf> request = new HttpEntity<>(myShelf);
        restTemplate.postForObject("http://localhost:9010/bookshelf/v1", request, BookShelf.class);
    }

	@Test
    public void bookShelfCRUDTest() {

        //We obtain all the shelves...
        ResponseEntity<BookShelf[]> responseBookShelves = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1", BookShelf[].class);
        BookShelf[] bookShelves = responseBookShelves.getBody();

        //... and we see that we have one.
        assertTrue(bookShelves.length == 1);

        //We add a book to the shelf...
        bookShelves[0].addBook(new Book("testTitle", "testISBN"));
        HttpEntity<BookShelf> request = new HttpEntity<>(bookShelves[0]);
        restTemplate.postForObject("http://localhost:9010/bookshelf/v1", request, BookShelf.class);

        //... and check that it was changed
        ResponseEntity<BookShelf> responseBookShelf = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId(), BookShelf.class);
        assertTrue(responseBookShelf.getBody().getBooks().size() == 3);

        //We also check that we have one more book.
        ResponseEntity<Book[]> response = restTemplate.getForEntity("http://localhost:9010/book/v1", Book[].class);
        assertTrue(response.getBody().length == 3);

        //We now delete that shelf...
        restTemplate.delete("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId());

        //... and see it was deleted.
        responseBookShelf = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId(), BookShelf.class);
        assertNull(responseBookShelf.getBody());
    }

	@Test
    public void bookCRUDTest() {

        //We obtain all the books...
        ResponseEntity<Book[]> responseBooks = restTemplate.getForEntity("http://localhost:9010/book/v1", Book[].class);
        Book[] books = responseBooks.getBody();

        //... and we see that we have two.
        assertTrue(books.length == 2);

        //We change the isbn of the first book and add an author for it...
        books[0].setIsbn("testISBN");
        books[0].addAuthor(new Author("firstName", "lastName"));
        HttpEntity<Book> request = new HttpEntity<>(books[0]);
        restTemplate.postForObject("http://localhost:9010/book/v1", request, Book.class);

        //... and check that it was changed
        ResponseEntity<Book> responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/" + books[0].getId(), Book.class);
        assertTrue(responseBook.getBody().getIsbn().equals("testISBN"));
        assertTrue(responseBook.getBody().getAuthors().size() == 2);
        
        //We also check that we have one more author.
        ResponseEntity<Author[]> response = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        assertTrue(response.getBody().length == 3);

        //We now delete that book...
        restTemplate.delete("http://localhost:9010/book/v1/" + books[0].getId());

        //... and see it was deleted.
        responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/" + books[0].getId(), Book.class);
        assertNull(responseBook.getBody());

        //We also see that in the book shelf we only have one book.
        ResponseEntity<BookShelf[]> responseFinal = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1", BookShelf[].class);
        assertTrue(responseFinal.getBody()[0].getBooks().size() == 1);
    }

	@Test
    public void authorCRUDTest()
	{
        //We obtain all the authors...
        ResponseEntity<Author[]> responseAuthors = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        Author[] authors = responseAuthors.getBody();

        //... and we see that we have two.
        assertTrue(authors.length == 2);

        //We change the firstName of the first author and add a book for it...
        authors[0].setFirstName("testFirstName");
        authors[0].addBook(new Book("testTitle", "testISBN"));
        HttpEntity<Author> request = new HttpEntity<>(authors[0]);
        restTemplate.postForObject("http://localhost:9010/author/v1", request, Author.class);

        //... and check that it was changed
        ResponseEntity<Author> responseAuthor = restTemplate.getForEntity("http://localhost:9010/author/v1/" + authors[0].getId(), Author.class);
        assertTrue(responseAuthor.getBody().getFirstName().equals("testFirstName"));
        assertTrue(responseAuthor.getBody().getBooks().size() == 2);

        //We look that we have one more book.
        ResponseEntity<Book[]> response = restTemplate.getForEntity("http://localhost:9010/book/v1", Book[].class);
        assertTrue(response.getBody().length == 3);

        //We now delete that author...
        restTemplate.delete("http://localhost:9010/author/v1/" + authors[0].getId());

        //... and see it was deleted.
        responseAuthor = restTemplate.getForEntity("http://localhost:9010/author/v1/" + authors[0].getId(), Author.class);
        assertNull(responseAuthor.getBody());

        //We also see that we have only one author.
        ResponseEntity<Author[]> responseFinal = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        assertTrue(responseFinal.getBody().length == 1);
	}

	@After
	public void release()
	{

	}

}
