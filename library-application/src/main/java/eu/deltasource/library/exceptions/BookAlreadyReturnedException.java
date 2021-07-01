package eu.deltasource.library.exceptions;

/**
 * Thrown when an attempt to return book that is already returned is made.
 */
public class BookAlreadyReturnedException extends RuntimeException {

    public BookAlreadyReturnedException(String message) {
        super(message);
    }
}
