package io.lion5.security.bookshelf.books;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a book to API consumers.
 */
public final class Book {

    /** internal ID of the book */
    private final long id;

    /** title of the book */
    private final String title;
    /** author of the book */
    private final String author;

    /** date the book was published at */
    private final LocalDate publishedDate;

    @JsonCreator
    public Book(@JsonProperty("id") long id,
                @JsonProperty("title") String title,
                @JsonProperty("author") String author,
                @JsonProperty("publishedDate") LocalDate publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }
}
