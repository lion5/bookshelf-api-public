package io.lion5.security.bookshelf.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints in the API for CRUD (Create, Read, Update, Delete) operations on books.
 */
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/created-by/{user}")
    public List<Book> getAllBooksOfUser(@PathVariable("user") String user) {
        return bookService.getBooksOfUser(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> createBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.createBook(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") long id) {
        return ResponseEntity.of(bookService.getBookById(id));
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<BookAudit> getBookAudit(@PathVariable("id") long id) {
        return ResponseEntity.of(bookService.getBookAuditById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
        return ResponseEntity.of(bookService.updateBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable("id") long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
