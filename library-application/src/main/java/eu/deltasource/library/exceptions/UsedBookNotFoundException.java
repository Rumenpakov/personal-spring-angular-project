package eu.deltasource.library.exceptions;

/**
 * Thrown when used book is searched in user history but is not found.
 */
public class UsedBookNotFoundException extends RuntimeException {

    public UsedBookNotFoundException(String message) {
        super(message);
    }
}
