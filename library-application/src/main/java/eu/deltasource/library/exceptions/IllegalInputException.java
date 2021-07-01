package eu.deltasource.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when illegal(Empty or null) input has been given.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalInputException extends RuntimeException{
    public IllegalInputException(String message) {
        super(message);
    }
}
