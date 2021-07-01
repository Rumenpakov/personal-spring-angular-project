package eu.deltasource.library.exceptions;

/**
 * Thrown when user that is already in queue for borrow book is attempted to be added again.
 */
public class UserAlreadyExistsInQueueException extends RuntimeException {

    public UserAlreadyExistsInQueueException(String message) {
        super(message);
    }
}
