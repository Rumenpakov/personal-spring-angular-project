package eu.deltasource.library.exceptions;

/**
 * Thrown when request is made that requires active session, without such being present.
 */
public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
