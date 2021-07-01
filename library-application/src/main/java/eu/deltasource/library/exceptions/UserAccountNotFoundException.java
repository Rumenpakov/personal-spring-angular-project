package eu.deltasource.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an attempt to retrieve the currently logged in user is made,
 * but there is no currently logged in user.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException(String s) {
    }
}
