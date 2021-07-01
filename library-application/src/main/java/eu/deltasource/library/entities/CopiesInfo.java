package eu.deltasource.library.entities;

import eu.deltasource.library.exceptions.IllegalBorrowedCopiesIncrementException;
import eu.deltasource.library.exceptions.IllegalInputException;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Contains information about copies of {@link PaperBookInfo}
 */
@Entity
@NoArgsConstructor
public class CopiesInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int totalCopiesCount;
    private int borrowedCopiesCount = 0;

    public CopiesInfo(int totalCopiesCount) {
        setTotalCopiesCount(totalCopiesCount);
    }

    private void setTotalCopiesCount(int totalCopiesCount) {
        if (totalCopiesCount < 0) {
            throw new IllegalInputException("Total copies count can not be less than 1. Value passed: "
                    + totalCopiesCount);
        }
        this.totalCopiesCount = totalCopiesCount;
    }

    public int getTotalCopiesCount() {
        return totalCopiesCount;
    }

    public int getAvailableCopies() {
        return totalCopiesCount - borrowedCopiesCount;
    }

    /**
     * Increments borrowed copies when book is borrowed by user.
     *
     * @throws IllegalBorrowedCopiesIncrementException when borrowed copies will become more than total copies.
     */
    public void incrementBorrowedCopies() {
        if (borrowedCopiesCount >= totalCopiesCount) {
            throw new IllegalBorrowedCopiesIncrementException("There are no more available copies to borrow");
        }
        borrowedCopiesCount++;
    }

    /**
     * Decrements borrowed copies when book is returned by user.
     *
     * @throws eu.deltasource.library.exceptions.IllegalBorrowedCopiesDecrementException when borrowed copeis will become less than 0.
     */
    public void decrementBorrowedCopies() {
        if (borrowedCopiesCount < 0) {
            throw new IllegalBorrowedCopiesIncrementException("There are no more available copies to borrow");
        }
        borrowedCopiesCount--;
    }
}
