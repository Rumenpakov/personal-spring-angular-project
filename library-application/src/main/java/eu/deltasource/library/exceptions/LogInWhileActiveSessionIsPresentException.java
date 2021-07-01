package eu.deltasource.library.exceptions;

/**
 * Thrown when log in attempt is made while another user is currently logged in.
 */
public class LogInWhileActiveSessionIsPresentException extends RuntimeException {

    public LogInWhileActiveSessionIsPresentException(String message) {
        super(message);
    }
}
