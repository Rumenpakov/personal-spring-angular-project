package eu.deltasource.library.exceptions;

/**
 * Thrown when attempt to decrement borrowed copies will result in borrowed copies less than 0.
 */
public class IllegalBorrowedCopiesDecrementException extends RuntimeException {

    public IllegalBorrowedCopiesDecrementException(String message) {
        super(message);
    }
}
