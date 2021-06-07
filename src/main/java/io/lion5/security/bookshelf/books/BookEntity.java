package io.lion5.security.bookshelf.books;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a book as stored in the database.
 */
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private String lastUpdatedBy;

    public BookEntity() {
    }

    public BookEntity(String title, String author, LocalDate publishedDate, String createdBy) {
        this.id = null;
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.createdBy = createdBy;
        this.lastUpdatedBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
