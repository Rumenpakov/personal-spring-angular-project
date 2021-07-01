package eu.deltasource.library.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.eventSources.DayLapseEventSource;
import eu.deltasource.library.exceptions.IllegalInputException;
import eu.deltasource.library.exceptions.UserNotFoundAsHolderOfBookException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * Contains all necessary information about paper book.
 * Also contains method necessary for making borrow requests by users to borrow the book.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PaperBookInfo implements Borrowable, DayLapseObserver {

    private static final int MAXIMUM_BORROW_PERIOD_WITH_POSTPONEMENT = 28;
    private static final int INDEX_OF_FIRST_USER_IN_QUEUE = 0;
    private static final int MAXIMUM_ALLOWED_DAYS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int availableCopies;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BookStatus> statusOfBooks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UserAccount> queueForBookBorrow = new LinkedList<>();

    @OneToOne
    private Book book;
    private LocalDate dateNow = LocalDate.now();

    @JsonCreator
    public PaperBookInfo(int availableCopies, Book book) {
        this.availableCopies = availableCopies;
        populateBookUsers(availableCopies);
        setBook(book);
        DayLapseEventSource.getInstance().addObserver(this);
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    /**
     * Creates {@link BookStatus} object for each copy of the book.
     *
     * @param initialCopiesCount Copies count
     */
    private void populateBookUsers(int initialCopiesCount) {
        if (initialCopiesCount < 1) {
            throw new IllegalInputException("Initial copies count can not be less than 1. Value passed: "
                    + initialCopiesCount);
        }

        Stream.iterate(0, i -> i + 1)
                .limit(initialCopiesCount)
                .forEach(integer -> statusOfBooks.add(new BookStatus()));
    }

    /**
     * Processes borrow request and returns the result, as well as estimated date when the book will be available to
     * be borrowed if the request is not successful.
     *
     * @param userAccount The user that makes the borrow request.
     * @return The result of the borrow request
     */
    @Override
    public BorrowRequestDetails processBorrowRequest(UserAccount userAccount) {
        Optional<BookStatus> bookLockedForUser = getBookLockedForUser(userAccount);
        Optional<BookStatus> firstAvailableCopy = getFirstAvailableCopy();
        if (bookLockedForUser.isPresent()) {
            bookLockedForUser.get().setBorrowerDetails(userAccount);
            return createBorrowRequestDetailsForSuccessfulBorrow();
        } else if (firstAvailableCopy.isPresent()) {
            firstAvailableCopy.get().setBorrowerDetails(userAccount);
            return createBorrowRequestDetailsForSuccessfulBorrow();
        }
        addUserToQueueIfNotPresent(userAccount);
        return getBorrowRequestDetailsForUnsuccessfulBorrow(userAccount);

    }

    /**
     * Creates borrow request details for successful borrow.
     *
     * @return Borrow request details
     */
    private BorrowRequestDetails createBorrowRequestDetailsForSuccessfulBorrow() {
        availableCopies--;
        return new BorrowRequestDetails(BorrowRequestStatus.SUCCESSFUL, book);
    }

    /**
     * Returns the index of the first available book.
     *
     * @return Index of first available book or -1 if there is no currently available book.
     */
    private Optional<BookStatus> getFirstAvailableCopy() {
        return statusOfBooks.stream()
                .filter(bookStatus -> !bookStatus.isBorrowed())
                .findFirst();
    }

    /**
     * Checks if there is book locked for given user.
     *
     * @param userAccount User that we are checking if book is locked for him.
     * @return -1 if not present, index otherwise
     */
    private Optional<BookStatus> getBookLockedForUser(UserAccount userAccount) {
        return statusOfBooks.stream()
                .filter(bookStatus -> isBookUsedByUser(bookStatus) && isUserMatching(userAccount, bookStatus))
                .findFirst();
    }

    private boolean isUserMatching(UserAccount userAccount, BookStatus bookStatus) {
        return bookStatus.getBookUser().equals(userAccount);
    }

    private boolean isBookUsedByUser(BookStatus bookStatus) {
        return bookStatus.getBookUser() != null;
    }

    /**
     * Adds user to queue for borrow book if not present.
     *
     * @param userAccount User account that made the borrow request.
     */
    private void addUserToQueueIfNotPresent(UserAccount userAccount) {
        if (!queueForBookBorrow.contains(userAccount)) {
            queueForBookBorrow.add(userAccount);
        }
    }

    /**
     * Creates {@link BorrowRequestDetails} object for {@link UserAccount} that made the request.
     * This object will contain estimated period of time when the book will be available to be borrowed.
     *
     * @param userAccount {@link UserAccount} that made the borrow request.
     * @return Information about the borrow request.
     */
    private BorrowRequestDetails getBorrowRequestDetailsForUnsuccessfulBorrow(UserAccount userAccount) {
        int userPositionInQueue = queueForBookBorrow.indexOf(userAccount);
        return new BorrowRequestDetails(BorrowRequestStatus.IN_QUEUE,
                findEstimatedWaitDays(userPositionInQueue));
    }

    /**
     * This method is used to calculate the estimated days when a user will be able to borrow the requested book.
     * The calculation is performed based on his position in queue.
     *
     * @param positionInQueue Position in queue for borrow book
     * @return Estimated days
     */
    private int findEstimatedWaitDays(int positionInQueue) {
        int smallPosition = positionInQueue % statusOfBooks.size();
        int bigPosition = positionInQueue / statusOfBooks.size();
        return MAXIMUM_BORROW_PERIOD_WITH_POSTPONEMENT - statusOfBooks.get(smallPosition).
                getDaysElapsedSinceBookBorrow(dateNow) + MAXIMUM_BORROW_PERIOD_WITH_POSTPONEMENT * bigPosition;
    }

    /**
     * This method is called from {@link DayLapseEventSource} when day has elapsed.
     */
    @Override
    public void dayLapsed() {
        dateNow = dateNow.plusDays(1);
        removeUsersWithExpiredLockedBooksAndLockForNextUserInQueueIfPresent();
    }

    /**
     * Frees all currently locked books if the three days have passed since the book was locked, and locks it
     * for the next user in queue if such is present.
     */
    private void removeUsersWithExpiredLockedBooksAndLockForNextUserInQueueIfPresent() {
        Iterator<BookStatus> bookStatusIterator = statusOfBooks.listIterator();
        BookStatus bookStatus;
        while (bookStatusIterator.hasNext()) {
            bookStatus = bookStatusIterator.next();
            if (bookStatus.isLockedForUser() && hasBookBeenLockedForUserMoreThanTheAllowedDays(bookStatus)) {
                moveBookToFrontOfList(bookStatus);
                bookStatus.freeBook();
                lockBookForNextUserInQueueIfPresent(bookStatus);
            }
        }
    }

    private void moveBookToFrontOfList(BookStatus bookStatus) {
        statusOfBooks.remove(bookStatus);
        statusOfBooks.add(0, bookStatus);
    }

    /**
     * Checks if the maximum allowed days have passed since the book was locked for user.
     *
     * @param bookStatus
     * @return
     */
    private boolean hasBookBeenLockedForUserMoreThanTheAllowedDays(BookStatus bookStatus) {
        return bookStatus.daysElapsedSinceBookWasLocked(dateNow) > MAXIMUM_ALLOWED_DAYS;
    }

    /**
     * Performs necessary actions, such as removing his information from the book that he borrowed.
     *
     * @param userAccount The user that returns the book
     */
    public void userReturnsBook(UserAccount userAccount) {
         Optional<BookStatus> bookStatusOptional = statusOfBooks.stream()
                .filter(item -> userAccount.equals(item.getBookUser()))
                .findAny();
         bookStatusOptional.orElseThrow(() -> new UserNotFoundAsHolderOfBookException("Book can not be returned if " +
                 "user doesnt have a copy of it. Value passed: " + userAccount));
         BookStatus bookStatus = bookStatusOptional.get();
         availableCopies++;
         moveBookToFrontOfList(bookStatus);
         bookStatus.onBookReturnedByUser();
         lockBookForNextUserInQueueIfPresent(bookStatus);
    }

    /**
     * Locks a book for the next user in queue if such is present.
     *
     * @param bookStatus The book to be locked.
     */
    private void lockBookForNextUserInQueueIfPresent(BookStatus bookStatus) {
        if (!queueForBookBorrow.isEmpty()) {
            UserAccount userAccount = queueForBookBorrow.get(INDEX_OF_FIRST_USER_IN_QUEUE);
            userAccount.notify(NotificationType.BOOK_RESERVED);
            bookStatus.lockBookForUser(userAccount);
            queueForBookBorrow.remove(INDEX_OF_FIRST_USER_IN_QUEUE);
        }
    }

    @Override
    public String toString() {
        return "PaperBookInfo{" +
                "id=" + id +
                ", availableCopies=" + availableCopies +
                ", statusOfBooks=" + statusOfBooks +
                ", queueForBookBorrow=" + queueForBookBorrow +
                ", book=" + book +
                ", dateNow=" + dateNow +
                '}';
    }
}