package eu.deltasource.library.exceptions;

/**
 * Thrown when attempt to decrement borrowed copies will result in borrowed copies more than total copies.
 */
public class IllegalBorrowedCopiesIncrementException extends RuntimeException {

    public IllegalBorrowedCopiesIncrementException(String message) {
        super(message);
    }
}
