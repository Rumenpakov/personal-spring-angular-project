package eu.deltasource.library.exceptions;

/**
 * Thrown when location object is presented in not supported format.
 */
public class IllegalLocationException extends RuntimeException {

    public IllegalLocationException(String message) {
        super(message);
    }
}
