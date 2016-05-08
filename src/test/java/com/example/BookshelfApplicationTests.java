package com.example;

import com.example.dao.Author;
import com.example.dao.Book;
import com.example.dao.BookShelf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class BookshelfApplicationTests {
    final RestTemplate restTemplate = new RestTemplate();

    @Before
    public void setup() throws Exception {
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

        //We add another book to the self.
        final Book anotherBook = new Book("anotherTitle", "anotherISBN");
        HttpEntity<Book> requestInsertBook = new HttpEntity<>(anotherBook);
        restTemplate.postForObject("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId() + "/book", requestInsertBook, BookShelf.class);

        //We also check that we have two more books.
        responseBookShelf = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId(), BookShelf.class);
        assertTrue(responseBookShelf.getBody().getBooks().size() == 4);

        //We delete one book from the shelf.
        restTemplate.delete("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId() + "/book/1");

        //We have again three books int the shelf.
        responseBookShelf = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/" + bookShelves[0].getId(), BookShelf.class);
        assertTrue(responseBookShelf.getBody().getBooks().size() == 3);

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
        books[0].setAuthor(new Author("firstName", "lastName"));
        HttpEntity<Book> request = new HttpEntity<>(books[0]);
        restTemplate.postForObject("http://localhost:9010/book/v1", request, Book.class);

        //... and check that it was changed
        ResponseEntity<Book> responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/" + books[0].getId(), Book.class);
        assertTrue(responseBook.getBody().getIsbn().equals("testISBN"));
        assertTrue(responseBook.getBody().getAuthor().getFirstName().equals("firstName"));

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
    public void authorCRUDTest() {
        //We obtain all the authors...
        ResponseEntity<Author[]> responseAuthors = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        Author[] authors = responseAuthors.getBody();

        //... and we see that we have two.
        assertTrue(authors.length == 2);

        //We obtain the books of an author.
        ResponseEntity<Book[]> books = restTemplate.getForEntity("http://localhost:9010/author/v1/" + authors[0].getId() + "/book", Book[].class);
        assertTrue(books.getBody().length == 1);

        //We change the firstName of the first author...
        authors[0].setFirstName("testFirstName");
        HttpEntity<Author> request = new HttpEntity<>(authors[0]);
        restTemplate.postForObject("http://localhost:9010/author/v1", request, Author.class);

        //... and check that it was changed
        ResponseEntity<Author> responseAuthor = restTemplate.getForEntity("http://localhost:9010/author/v1/" + authors[0].getId(), Author.class);
        assertTrue(responseAuthor.getBody().getFirstName().equals("testFirstName"));

        //We now delete that author...
        restTemplate.delete("http://localhost:9010/author/v1/" + authors[0].getId());

        //... and see it was deleted.
        responseAuthor = restTemplate.getForEntity("http://localhost:9010/author/v1/" + authors[0].getId(), Author.class);
        assertNull(responseAuthor.getBody());

        //We also see that we have only one author.
        ResponseEntity<Author[]> responseFinal = restTemplate.getForEntity("http://localhost:9010/author/v1", Author[].class);
        assertTrue(responseFinal.getBody().length == 1);
    }

    @Test
    public void testBookShelfSearch() {
        //We get all the books of the book shelf.
        ResponseEntity<Book[]> responseBooks = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/1/book", Book[].class);
        assertTrue(responseBooks.getBody().length == 2);

        //We get all the books ordered by title asc.
        responseBooks = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/1/book?sortBy=title&sort=asc", Book[].class);
        assertTrue(responseBooks.getBody()[0].getTitle().equals("Hamlet"));
        assertTrue(responseBooks.getBody()[1].getTitle().equals("Quijote"));

        //We get all the books ordered by title desc.
        responseBooks = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/1/book?sortBy=title&sort=desc", Book[].class);
        assertTrue(responseBooks.getBody()[0].getTitle().equals("Quijote"));
        assertTrue(responseBooks.getBody()[1].getTitle().equals("Hamlet"));

        //We get all the books ordered by author asc.
        responseBooks = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/1/book?sortBy=lastName&sort=asc", Book[].class);
        assertTrue(responseBooks.getBody()[0].getAuthor().getLastName().equals("de Cervantes"));
        assertTrue(responseBooks.getBody()[1].getAuthor().getLastName().equals("Shakespeare"));

        //We get all the books ordered by author desc.
        responseBooks = restTemplate.getForEntity("http://localhost:9010/bookshelf/v1/1/book?sortBy=lastName&sort=desc", Book[].class);
        assertTrue(responseBooks.getBody()[0].getAuthor().getLastName().equals("Shakespeare"));
        assertTrue(responseBooks.getBody()[1].getAuthor().getLastName().equals("de Cervantes"));
    }

    @Test
    public void testFindBooksBy() {
        //Find by id was already tested in the corresponding CRUd test.

        //We find by title.
        ResponseEntity<Book[]> responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/by?getBy=title&value=Hamlet", Book[].class);
        assertTrue(responseBook.getBody()[0].getTitle().equals("Hamlet"));

        //We find by ISBN.
        responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/by?getBy=isbn&value=HamletISBN", Book[].class);
        assertTrue(responseBook.getBody()[0].getIsbn().equals("HamletISBN"));

        //We find by last name of the author.
        responseBook = restTemplate.getForEntity("http://localhost:9010/book/v1/by?getBy=lastName&value=Shakespeare", Book[].class);
        assertTrue(responseBook.getBody()[0].getIsbn().equals("HamletISBN"));
    }

    @After
    public void release() {

    }

}
