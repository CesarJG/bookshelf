package com.example;

import com.example.dao.Author;
import com.example.repository.AuthorRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookshelfApplication.class)
@WebAppConfiguration
public class BookshelfApplicationTests
{
	@Autowired
	private AuthorRepository authorRepository;

	@Before
	public void setup() throws Exception
	{
	}

	@Test
	public void contextLoads()
	{
	}

	@Test
	public void testEmbeddedDB()
	{
		authorRepository.save(new Author("Jack", "Bauer"));
		authorRepository.save(new Author("Chloe", "O'Brian"));

		for (Author customer : authorRepository.findAll()) {
			System.out.println(customer.toString());
		}
	}

	@After
	public void release()
	{

	}

}
