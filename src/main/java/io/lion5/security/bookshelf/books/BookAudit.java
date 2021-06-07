package io.lion5.security.bookshelf.books;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookAudit {

    private final long bookId;

    private final String createdBy;
    private final String lastUpdatedBy;

    @JsonCreator
    public BookAudit(@JsonProperty("bookId") long bookId,
                     @JsonProperty("createdBy") String createdBy,
                     @JsonProperty("lastUpdatedBy") String lastUpdatedBy) {
        this.bookId = bookId;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public long getBookId() {
        return bookId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
}
