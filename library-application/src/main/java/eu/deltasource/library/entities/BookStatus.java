package eu.deltasource.library.entities;

import eu.deltasource.library.exceptions.BookNotLockedForUserException;
import eu.deltasource.library.exceptions.UserNotFoundAsHolderOfBookException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This class is used to represent each book and its current status and borrower if such is present.
 * It contains information about when was the book borrowed, or when was it locked for a user to borrow in case
 * he was next in queue for the given book.
 */
@Data
@NoArgsConstructor
@Entity
public class BookStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private UserAccount bookUser;
    private LocalDate lockedDate = null;
    private LocalDate borrowDate;

    /**
     * Sets information about the borrower of the book, and the date it was borrowed.
     *
     * @param userAccount is the user that borrowed the book.
     */
    public void setBorrowerDetails(UserAccount userAccount) {
        this.bookUser = userAccount;
        borrowDate = LocalDate.now();
    }

    /**
     * This method is invoked when the holder (user) of the currently borrowed book returns it.
     */
    public void onBookReturnedByUser() {
        clearRecord();
    }

    /**
     * Clears all information about the book.
     */
    private void clearRecord() {
        bookUser = null;
        borrowDate = null;
        lockedDate = null;
    }

    /**
     * Calculates days elapsed since the book was first borrowed.
     *
     * @param currentDate The current date to calculate against.
     * @return Days elapsed since the book was borrowed.
     */
    public int getDaysElapsedSinceBookBorrow(LocalDate currentDate) {
        if (borrowDate == null) {
            throw new UserNotFoundAsHolderOfBookException("Can not get days elapsed since borrow for book not borrowed yet.");
        }
        return (int) DAYS.between(currentDate, borrowDate);
    }

    /**
     * Calculates days elapsed since the book was locked for user.
     *
     * @param currentDate The current date to calculate against.
     * @return Days elapsed since the book was locked.
     */
    public int daysElapsedSinceBookWasLocked(LocalDate currentDate) {
        if (lockedDate == null) {
            throw new BookNotLockedForUserException("Can not get days elapsed since book was locked for book that is not" +
                    " currently locked for user.");
        }
        return (int) DAYS.between(lockedDate, currentDate);
    }

    /**
     * Checks if the book is currently borrowed.
     *
     * @return True if the book is borrowed.
     */
    public boolean isBorrowed() {
        return bookUser != null;
    }

    /**
     * Checks if the book is currently locked for user.
     *
     * @return True if locked for user.
     */
    public boolean isLockedForUser() {
        return lockedDate != null;
    }

    /**
     * Returns the {@Link UserAccount} of the user that the book is currently borrowed by or is locked for.
     *
     * @return User account of whom the book is borrowed by / locked for.
     */
    public UserAccount getBookUser() {
        return bookUser;
    }

    /**
     * Locks book for user.
     * Sets information about the user that the book is locked for, as well as the date when the book was locked.
     *
     * @param userAccount The account of the user that the book will be locked for.
     */
    public void lockBookForUser(UserAccount userAccount) {
        this.bookUser = userAccount;
        lockedDate = LocalDate.now();
    }

    /**
     * Checks if the book is available to be borrowed.
     * A book is available when its not locked for user nor is borrowed by user.
     *
     * @return True if the book is available to be borrowed.
     */
    public boolean isAvailable() {
        return !isLockedForUser() && !isBorrowed();
    }

    /**
     * Sets the book free of a user.
     */
    public void freeBook() {
        clearRecord();
    }
}
