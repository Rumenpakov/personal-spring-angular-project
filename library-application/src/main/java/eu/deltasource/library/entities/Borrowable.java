package eu.deltasource.library.entities;

/**
 * Contains the method necessary for borrow request to be made.
 */
public interface Borrowable {

    BorrowRequestDetails processBorrowRequest(UserAccount user);
}
