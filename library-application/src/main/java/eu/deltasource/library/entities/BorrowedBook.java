package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.WayOfAcquisition;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Contains information about borrowed book, such as return date (if the book is returned) and postpone days
 * that mark the time that the user has asked for exceeding his allowed hold period.
 */
@NoArgsConstructor
@Entity

public class BorrowedBook extends UsedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate returnDate = null;
    private int postponeDays = 0;

    public BorrowedBook(Book book, LocalDate acquireDate) {
        super(book, acquireDate, WayOfAcquisition.BORROW);
    }

    public void setReturnDate(LocalDate localDate) {
        returnDate = localDate;
    }

    public Optional<LocalDate> getReturnDate() {
        return Optional.ofNullable(returnDate);
    }

    public int getPostponeDays() {
        return postponeDays;
    }

    public void addToPostponedDays(int daysToPostPone) {
        postponeDays += daysToPostPone;
    }
}
