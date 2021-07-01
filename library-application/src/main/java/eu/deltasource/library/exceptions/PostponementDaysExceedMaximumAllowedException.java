package eu.deltasource.library.exceptions;

/**
 * Thrown when an attempt for postponement that exceeds the maximum allowed postponement
 * days is made.
 */
public class PostponementDaysExceedMaximumAllowedException extends RuntimeException {

    public PostponementDaysExceedMaximumAllowedException(String message) {
        super(message);
    }
}
