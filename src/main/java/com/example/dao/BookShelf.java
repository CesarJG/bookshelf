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

public class BookShelf
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
    private String owner;

    @ManyToMany(mappedBy = "shelves", fetch = FetchType.EAGER)
    private Set<Book> books = new HashSet<Book>();

    /**
     *
     */
    public BookShelf()
	{
	}

    /**
     *
     * @param owner
     */
    public BookShelf(final String owner)
	{

        this.owner = owner;
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
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    public Set<Book> getBooks() {
        return books;
    }

    /**
     *
     * @param books
     */
    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookShelf bookShelf = (BookShelf) o;

        if (id != bookShelf.id) return false;
        if (owner != null ? !owner.equals(bookShelf.owner) : bookShelf.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
