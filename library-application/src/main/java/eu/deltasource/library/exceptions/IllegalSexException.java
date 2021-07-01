package eu.deltasource.library.exceptions;

import eu.deltasource.library.entities.enums.*;

/**
 * Thrown when sex object is presented in not supported format.
 */
public class IllegalSexException extends RuntimeException {

    public IllegalSexException(String message) {
        super(message);
    }
}
