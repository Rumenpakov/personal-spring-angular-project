package eu.deltasource.library.exceptions;

/**
 * Thrown when an operation requiring the book to be locked for a user account
 * is made, but it is not locked for the given user.
 */
public class BookNotLockedForUserException extends RuntimeException {

    public BookNotLockedForUserException(String message) {
        super(message);
    }
}
