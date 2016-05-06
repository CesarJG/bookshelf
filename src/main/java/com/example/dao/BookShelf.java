package com.example.dao;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CJuarez.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "uuid")
public class BookShelf
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private Set<Book> books = new HashSet<Book>();

	/**
     *
     */
	public BookShelf()
	{
	}

	/**
	 *
	 * @param book
	 */
	public void addBook(final Book book)
	{
		books.add(book);
		book.getShelves().add(this);
	}

	/**
	 *
	 * @param book
	 */
	public void removeBook(final Book book)
	{
		books.remove(book);
		book.getShelves().remove(this);
	}

	/**
     *
     */
	public void removeAllBooks(){
        for(final Book book : new ArrayList<>(books)) {
            removeBook(book);
        }
    }

	/**
	 *
	 * @return
	 */
	public long getId()
	{
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(final long id)
	{
		this.id = id;
	}

	/**
	 *
	 * @return
	 */
	public Set<Book> getBooks()
	{
		return books;
	}

	/**
	 *
	 * @param books
	 */
	public void setBooks(Set<Book> books)
	{
		this.books = books;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BookShelf bookShelf = (BookShelf) o;

		if (id != bookShelf.id)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return (int) (id ^ (id >>> 32));
	}
}
