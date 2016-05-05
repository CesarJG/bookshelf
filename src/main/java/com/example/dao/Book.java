package com.example.dao;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CJuarez.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property="uuid")
public class Book
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String title;
	private String isbn;

    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Author> authors = new HashSet<Author>();

    @ManyToMany(cascade =
            {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<BookShelf> shelves = new HashSet<BookShelf>();

    /**
     *
     */
    public Book() {}

    /**
     *
     * @param title
     * @param isbn
     */
    public Book(final String title,final String isbn)
	{
		this.title = title;
		this.isbn = isbn;
	}



    /**
     *
     * @param author
     */
    public void addAuthor(final Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    /**
     *
     * @param author
     */
    public void removeAuthor(final Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    /**
     *
     */
    public void removeAllAuthors(){
        for(final Author author : new ArrayList<>(authors)) {
            removeAuthor(author);
        }
    }

    /**
     *
     * @param bookShelf
     */
    public void addBookShelf(final BookShelf bookShelf) {
        shelves.add(bookShelf);
        bookShelf.getBooks().add(this);
    }

    /**
     *
     * @param bookShelf
     */
    public void removeBookShelf(final BookShelf bookShelf) {
        shelves.remove(bookShelf);
        bookShelf.getBooks().remove(this);
    }

    /**
     *
     */
    public void removeAllBookShelves(){
        for(final BookShelf bookShelf : new ArrayList<>(shelves)) {
            removeBookShelf(bookShelf);
        }
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     *
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     *
     * @return
     */
    public Set<Author> getAuthors() {
        return authors;
    }

    /**
     *
     * @param authors
     */
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    /**
     *
     * @return
     */
    public Set<BookShelf> getShelves() {
        return shelves;
    }

    /**
     *
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

        if (id != book.id) return false;
        if (isbn != null ? !isbn.equals(book.isbn) : book.isbn != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        return result;
    }
}
