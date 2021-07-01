package eu.deltasource.library.exceptions;

import eu.deltasource.library.entities.EBookInfo;

/**
 * Thrown when an attempt to download E-Book is made,
 * where the E-Book doesnt have a download link.
 */
public class AttemptToDownloadBookWithoutDownloadLinkException extends RuntimeException {

    public AttemptToDownloadBookWithoutDownloadLinkException(String message) {
        super(message);
    }
}
