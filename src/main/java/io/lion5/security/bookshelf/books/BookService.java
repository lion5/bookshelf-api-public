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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
                getUsername()
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

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<BookAudit> getBookAuditById(long id) {
        return bookRepository.findById(id)
                             .map(this::auditFromEntity);
    }

    public Optional<Book> updateBook(Book book) throws UnauthorizedModificationException {
        Optional<BookEntity> optionalBook = bookRepository.findById(book.getId());
        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        BookEntity entity = optionalBook.get();

        if (!canModify(entity)) {
            throw new UnauthorizedModificationException();
        }

        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setPublishedDate(book.getPublishedDate());
        entity.setLastUpdatedBy(getUsername());

        return Optional.of(fromEntity(bookRepository.save(entity)));
    }

    public void deleteBook(long id) throws UnauthorizedModificationException {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return;
        }

        if (!canModify(optionalBook.get())) {
            throw new UnauthorizedModificationException();
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

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        return authentication.getName();
    }

    private boolean canModify(BookEntity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }

        return entity.getAuthor().equals(authentication.getName());
    }
}
