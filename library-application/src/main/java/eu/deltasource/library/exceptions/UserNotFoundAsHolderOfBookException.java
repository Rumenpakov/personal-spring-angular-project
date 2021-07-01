package eu.deltasource.library.exceptions;

/**
 * Thrown when an operation that requires the book to be borrowed
 * already is made, but the book is not actually in the user borrowed books.
 */
public class UserNotFoundAsHolderOfBookException extends RuntimeException {

    public UserNotFoundAsHolderOfBookException(String message) {
        super(message);
    }
}
