package io.lion5.security.bookshelf.books;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides methods to access the database and perform operations on books.
 */
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    /**
     * @param createdBy the username to filter for.
     * @return all books created by the given user.
     */
    List<BookEntity> findAllByCreatedBy(String createdBy);

}
