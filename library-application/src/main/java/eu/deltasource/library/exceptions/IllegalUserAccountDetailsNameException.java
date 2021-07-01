package eu.deltasource.library.exceptions;

/**
 * Thrown when name is presented in unsupported format.
 */
public class IllegalUserAccountDetailsNameException extends RuntimeException {

    public IllegalUserAccountDetailsNameException(String message) {
        super(message);
    }
}
