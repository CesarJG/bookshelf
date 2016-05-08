package com.example.repository;

import com.example.dao.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by CJuarez.
 */
public interface BookRepository extends CrudRepository<Book, Long>, JpaRepository<Book, Long> {
    public List<Book> findByTitle(final String title);

    public List<Book> findByIsbn(final String isbn);

    @Query("SELECT b FROM Book b WHERE LOWER(b.author.lastName) = LOWER(:lastName)")
    public List<Book> findBooksByAuthorLastName(@Param("lastName") String lastName);
}
