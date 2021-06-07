package io.lion5.security.bookshelf.books;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements the business logic that should be applied to books.
 */
@Service
public class BookService {

    private final EntityManager entityManager;
    private final BookRepository bookRepository;

    @Autowired
    public BookService(EntityManager entityManager, BookRepository bookRepository) {
        this.entityManager = entityManager;
        this.bookRepository = bookRepository;
    }

    public Book createBook(CreateBookRequest createBookRequest) {
        BookEntity entity = new BookEntity(
                createBookRequest.getTitle(),
                createBookRequest.getAuthor(),
                createBookRequest.getPublishedDate(),
                "" // getAuthenticatedUser()
        );

        return fromEntity(bookRepository.save(entity));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll().stream()
                             .map(this::fromEntity)
                             .collect(Collectors.toUnmodifiableList());
    }

    public List<Book> getBooksOfUser(String username) {
        Query query = entityManager.createNativeQuery("SELECT id, title, author, published_date, created_by, last_updated_by FROM book WHERE created_by = '" + username + "';");

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<Book> books = new ArrayList<>();
        for (Object[] row : results) {
            books.add(new Book(
                    ((BigInteger) row[0]).longValueExact(),
                    (String) row[1],
                    (String) row[2],
                    ((Date) row[3]).toLocalDate()
            ));
        }

        return Collections.unmodifiableList(books);
    }

    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id)
                             .map(this::fromEntity);
    }

    public Optional<BookAudit> getBookAuditById(long id) {
        return bookRepository.findById(id)
                             .map(this::auditFromEntity);
    }

    public Optional<Book> updateBook(Book book) {
        Optional<BookEntity> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        BookEntity entity = optionalBook.get();

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPublishedDate(book.getPublishedDate());

        return Optional.of(fromEntity(bookRepository.save(entity)));
    }

    public void deleteBook(long id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return;
        }

        bookRepository.delete(optionalBook.get());
    }

    private Book fromEntity(BookEntity entity) {
        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getPublishedDate()
        );
    }

    private BookAudit auditFromEntity(BookEntity entity) {
        return new BookAudit(
                entity.getId(),
                entity.getCreatedBy(),
                entity.getLastUpdatedBy()
        );
    }
}
