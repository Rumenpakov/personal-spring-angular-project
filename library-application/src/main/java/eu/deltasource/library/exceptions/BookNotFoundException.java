package eu.deltasource.library.exceptions;

/**
 * Thrown when book that is being searched is not found.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}
