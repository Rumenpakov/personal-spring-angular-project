package eu.deltasource.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when log out attempt is made, but no user is currently logged in.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LogOutWithoutLoggedInUserException extends RuntimeException {

    public LogOutWithoutLoggedInUserException(String message) {
        super(message);
    }
}
