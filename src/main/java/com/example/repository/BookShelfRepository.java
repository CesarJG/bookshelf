package com.example.repository;

import com.example.dao.Book;
import com.example.dao.BookShelf;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by CJuarez.
 */
public interface BookShelfRepository extends CrudRepository<BookShelf, Long>
{
}
