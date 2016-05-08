package com.example.dao;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CJuarez.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "uuid")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String isbn;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Author author;

    @ManyToMany(mappedBy = "books", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<BookShelf> shelves = new HashSet<BookShelf>();

    /**
     *
     */
    public Book() {
    }

    /**
     * @param title
     * @param isbn
     */
    public Book(final String title, final String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * @return
     */
    public Set<BookShelf> getShelves() {
        return shelves;
    }

    /**
     * @param shelves
     */
    public void setShelves(Set<BookShelf> shelves) {
        this.shelves = shelves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (isbn != null ? !isbn.equals(book.isbn) : book.isbn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return isbn != null ? isbn.hashCode() : 0;
    }
}
