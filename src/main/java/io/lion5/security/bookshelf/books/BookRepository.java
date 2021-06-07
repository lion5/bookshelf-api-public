package io.lion5.security.bookshelf.books;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides methods to access the database and perform operations on books.
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}
