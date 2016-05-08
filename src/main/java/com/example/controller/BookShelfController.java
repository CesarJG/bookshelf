package com.example.controller;

import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.service.BookShelfService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CJuarez.
 */
@RestController
@RequestMapping(value = "/bookshelf/v1")
public class BookShelfController {
    @Autowired
    private BookShelfService bookShelfService;

    @ApiOperation(value = "getBookShelf")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookShelf getBookShelf(@PathVariable("id") final long id) {
        return bookShelfService.findBookShelf(id);
    }

    @ApiOperation(value = "getBookShelves")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookShelf> getBookShelves() {
        return bookShelfService.findAllBookShelves();
    }

    @ApiOperation(value = "saveBookShelf")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookShelf saveBookShelf(@RequestBody final BookShelf bookShelf) {
        return bookShelfService.saveBookShelf(bookShelf);
    }

    @ApiOperation(value = "saveBookIntoBookShelf")
    @RequestMapping(value = "/{shelfId}/book", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookShelf saveBookIntoBookShelf(@PathVariable("shelfId") final long shelfId, @RequestBody final Book book) {
        return bookShelfService.saveBookIntoShelf(shelfId, book);
    }

    @ApiOperation(value = "getBooksOfBookShelf")
    @RequestMapping(value = "/{shelfId}/book", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBooksOfBookShelf(@PathVariable("shelfId") final long shelfId,
                                          @ApiParam(value = "Field to sort by. Possible values: title, lastName")
                                          @RequestParam(value = "sortBy", required = false) final String sortBy,
                                          @ApiParam(value = "Defines the order of the books. Possible values: asc, desc")
                                          @RequestParam(value = "sort", required = false) final String sort) {
        return bookShelfService.getBooksOfBookShelf(shelfId, sortBy, sort);
    }

    @ApiOperation(value = "deleteBookShelf")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBookShelf(@PathVariable("id") final long id) {
        bookShelfService.deleteBookShelf(id);
    }

    @ApiOperation(value = "deleteBookFromBookShelf")
    @RequestMapping(value = "/{shelfId}/book/{bookId}", method = RequestMethod.DELETE)
    public void deleteBookFromBookShelf(@PathVariable("shelfId") final long shelfId, @PathVariable("bookId") final long bookId) {
        bookShelfService.deleteBookFromBookShelf(shelfId, bookId);
    }
}
