package com.example.repository;

import com.example.dao.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by CJuarez.
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> findByLastName(String lastName);
}
