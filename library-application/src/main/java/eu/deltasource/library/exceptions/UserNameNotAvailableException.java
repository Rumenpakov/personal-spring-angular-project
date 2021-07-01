package eu.deltasource.library.exceptions;

/**
 * Thrown when user account is attempted to be added to repository, but repository already contains account
 * with same name.
 */
public class UserNameNotAvailableException extends RuntimeException {

    public UserNameNotAvailableException(String message) {
        super(message);
    }
}
