package eu.deltasource.library.exceptions;

/**
 * Thrown when an age less than 0 is given as user account detail.
 */
public class IllegalAgeException extends RuntimeException {

    public IllegalAgeException(String message) {
        super(message);
    }
}
