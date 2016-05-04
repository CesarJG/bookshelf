package com.example.controller;

import com.example.dao.Author;
import com.example.repository.AuthorRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CJuarez on 04.05.2016.
 */
@RestController
@RequestMapping(value = "/bookshelf/author/v1")
public class AuthorController
{
	@Autowired
	private AuthorRepository authorRepository;

	@ApiOperation(value = "getAuthor")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author getAuthor(@PathVariable("id") final long id)
	{
		return authorRepository.findOne(id);
	}

	@ApiOperation(value = "saveAuthor")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Author saveAuthor(@RequestBody final Author author)
	{
		return authorRepository.save(author);
	}
}
