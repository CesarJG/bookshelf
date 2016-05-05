package com.example.controller;

import com.example.dao.Author;
import com.example.repository.AuthorRepository;
import com.example.service.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by CJuarez.
 */
@RestController
@RequestMapping(value = "/author/v1")
public class AuthorController
{
	@Autowired
	private AuthorService authorService;

	@ApiOperation(value = "getAuthor")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author getAuthor(@PathVariable("id") final long id)
	{
		return authorService.findAuthor(id);
	}

    @ApiOperation(value = "getAuthors")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAuthors()
    {
        return authorService.findAllAuthors();
    }

	@ApiOperation(value = "saveAuthor")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author saveAuthor(@RequestBody final Author author) throws JsonProcessingException {
		return authorService.saveAuthor(author);
	}

    @ApiOperation(value = "deleteAuthor")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAuthor(@PathVariable("id") final long id)
    {
        authorService.deleteAuthor(id);
    }
}
