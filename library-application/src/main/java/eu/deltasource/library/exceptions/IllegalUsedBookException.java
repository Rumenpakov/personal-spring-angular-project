package eu.deltasource.library.exceptions;

/**
 * Thrown when an attempt to add used book to the used books of user is made,
 * but the book is already present there.
 */
public class IllegalUsedBookException extends RuntimeException {

    public IllegalUsedBookException(String message) {
        super(message);
    }
}
