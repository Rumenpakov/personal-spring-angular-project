package eu.deltasource.library.exceptions;

/**
 * Thrown when address is not presented in supported format.
 */
public class IllegalAddressException extends RuntimeException {

    public IllegalAddressException(String message) {
        super(message);
    }
}
