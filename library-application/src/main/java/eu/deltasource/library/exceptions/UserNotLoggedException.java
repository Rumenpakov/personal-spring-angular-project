package eu.deltasource.library.exceptions;

/**
 * Thrown when operation such as borrow book is attempted by a user, but no account is currently logged in.
 */
public class UserNotLoggedException extends RuntimeException {

    public UserNotLoggedException(String message) {
        super(message);
    }
}
