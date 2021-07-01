package eu.deltasource.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when adding new book to repository that already contains the same book.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NewBookToAddAlreadyExistsException extends RuntimeException {

    public NewBookToAddAlreadyExistsException(String message) {
        super(message);
    }
}
