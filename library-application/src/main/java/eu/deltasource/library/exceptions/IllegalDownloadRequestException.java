package eu.deltasource.library.exceptions;

/**
 * Thrown when downlaod request is made to a book that doesnt provide downlaod link.
 */
public class IllegalDownloadRequestException extends RuntimeException {

    public IllegalDownloadRequestException(String message) {
        super(message);
    }
}
