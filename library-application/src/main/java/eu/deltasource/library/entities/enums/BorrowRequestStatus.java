package eu.deltasource.library.entities.enums;

/**
 * Indicates the status of a borrow request. Successful if the request is successful,
 * and in queue, if the user has been put / or currently is in queue for the requested book
 */
public enum BorrowRequestStatus {
    SUCCESSFUL,
    IN_QUEUE
}