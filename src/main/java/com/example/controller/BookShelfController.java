package com.example.controller;

import com.example.dao.Book;
import com.example.dao.BookShelf;
import com.example.service.BookService;
import com.example.service.BookShelfService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by CJuarez.
 */
@RestController
@RequestMapping(value = "/bookshelf/v1")
public class BookShelfController
{
    @Autowired
    private BookShelfService bookShelfService;

    @ApiOperation(value = "getBookShelf")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookShelf getBookShelf(@PathVariable("id") final long id)
    {
        return bookShelfService.findBookShelf(id);
    }

    @ApiOperation(value = "getBookShelves")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookShelf> getBookShelves()
    {
        return bookShelfService.findAllBookShelves();
    }

    @ApiOperation(value = "saveBookShelf")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookShelf saveBookShelf(@RequestBody final BookShelf bookShelf)
    {
        return bookShelfService.saveBookShelf(bookShelf);
    }

    @ApiOperation(value = "deleteBookShelf")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteBookShelf(@PathVariable("id") final long id)
    {
        bookShelfService.deleteBookShelf(id);
    }
}
