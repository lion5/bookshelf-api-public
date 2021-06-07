package io.lion5.security.bookshelf.books;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the data provided by users in a request intending to create a new book.
 */
public final class CreateBookRequest {

    private final String title;
    private final String author;

    private final LocalDate publishedDate;

    @JsonCreator
    public CreateBookRequest(@JsonProperty("title") String title,
                             @JsonProperty("author") String author,
                             @JsonProperty("publishedDate") LocalDate publishedDate) {

        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
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
