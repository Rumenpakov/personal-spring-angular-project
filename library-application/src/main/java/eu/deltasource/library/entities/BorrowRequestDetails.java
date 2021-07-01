package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.exceptions.IllegalInputException;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * Contains borrow request information for Borrow request made by user to {@link PaperBookInfo}.
 */
@Entity
@NoArgsConstructor
public class BorrowRequestDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BorrowRequestStatus borrowRequestStatus;
    private int estimatedDaysUntilBorrow;
    @OneToOne
    private Book book;

    public BorrowRequestDetails(BorrowRequestStatus borrowRequestStatus, Book book) {
        this(borrowRequestStatus, 0, book);
    }

    public BorrowRequestDetails(BorrowRequestStatus borrowRequestStatus, int estimatedDaysUntilBorrow, Book book) {
        setBorrowRequestStatus(borrowRequestStatus);
        setEstimatedDaysUntilBorrow(estimatedDaysUntilBorrow);
        setBook(book);
    }

    public BorrowRequestDetails(BorrowRequestStatus borrowRequestStatus, int estimatedDaysUntilBorrow) {
        this(borrowRequestStatus, estimatedDaysUntilBorrow, null);
    }

    private void setBorrowRequestStatus(BorrowRequestStatus borrowRequestStatus) {
        if (borrowRequestStatus == null) {
            throw new IllegalInputException("Borrow request status can not be null for new borrow request detials");
        }
        this.borrowRequestStatus = borrowRequestStatus;
    }

    private void setEstimatedDaysUntilBorrow(int estimatedDaysUntilBorrow) {
        if (estimatedDaysUntilBorrow < 0) {
            throw new IllegalInputException("Estimated maximum days until borrow can not be less than 0");
        }
        this.estimatedDaysUntilBorrow = estimatedDaysUntilBorrow;
    }

    private void setBook(Book book) {
        this.book = book;
    }

    public BorrowRequestStatus getBorrowRequestStatus() {
        return borrowRequestStatus;
    }

    public int getEstimatedDaysUntilBorrow() {
        return estimatedDaysUntilBorrow;
    }

    public Book getBook() {
        return book;
    }

    /**
     * Checks if the request for borrow is successful.
     *
     * @param borrowRequestDetails The result of the request
     * @return True if the borrow was successful
     */
    public static boolean isBorrowSuccessful(BorrowRequestDetails borrowRequestDetails) {
        return borrowRequestDetails.getBorrowRequestStatus() == BorrowRequestStatus.SUCCESSFUL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof BorrowRequestDetails) {
            BorrowRequestDetails that = (BorrowRequestDetails) o;
            return estimatedDaysUntilBorrow == that.estimatedDaysUntilBorrow &&
                    borrowRequestStatus == that.borrowRequestStatus &&
                    Objects.equals(book, that.book);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowRequestStatus, estimatedDaysUntilBorrow, book);
    }

    @Override
    public String toString() {
        return "BorrowRequestDetails{" +
                "borrowRequestStatus=" + borrowRequestStatus +
                ", estimatedMaximumDaysUntilBorrow=" + estimatedDaysUntilBorrow +
                ", book=" + book +
                '}';
    }
}
