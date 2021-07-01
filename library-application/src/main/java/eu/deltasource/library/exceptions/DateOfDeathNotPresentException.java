package eu.deltasource.library.exceptions;

/**
 * Thrown when attempt to retrieve date of death is made but the author is still alive.
 */
public class DateOfDeathNotPresentException extends RuntimeException {

    public DateOfDeathNotPresentException(String message) {
        super(message);
    }
}
