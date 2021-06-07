package io.lion5.security.bookshelf.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signals that the requester can not execute the requested operation on the book.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Only the owner or admins can modify books.")
public class BookOwnerException extends Exception {
}
