package com.example.controller;

import com.example.dao.Book;
import com.example.service.BookService;
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
@RequestMapping(value = "/book/v1")
public class BookController {
    @Autowired
    private BookService bookService;

    @ApiOperation(value = "getBook")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBook(@PathVariable("id") final long id) {
        return bookService.findBook(id);
    }

    @ApiOperation(value = "getBooksByField")
    @RequestMapping(value = "/by", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBooksByField(
            @ApiParam(value = "Field to search by. Possible values: title, isbn, lastName")
            @RequestParam(value = "getBy", required = true) final String getBy,
            @ApiParam(value = "Value of the getBy field to search for")
            @RequestParam(value = "value", required = true) final String value) {
        return bookService.getBooksBy(getBy, value);
    }

    @ApiOperation(value = "getBooks")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBooks() {
        return bookService.findAllBooks();
    }

    @ApiOperation(value = "saveBook")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Book saveBook(@RequestBody final Book book) {
        return bookService.saveBook(book);
    }

    @ApiOperation(value = "deleteBook")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBook(@PathVariable("id") final long id) {
        bookService.deleteBook(id);
    }
}
