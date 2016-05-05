package com.example.repository;

import com.example.dao.Author;
import com.example.dao.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by CJuarez.
 */
public interface BookRepository extends CrudRepository<Book, Long>
{
}
