package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.WayOfAcquisition;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Contains information about used book, such as date and way of acquisition.
 */
@NoArgsConstructor
@Entity
public class UsedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Book book;
    private LocalDate acquireDate;
    private WayOfAcquisition wayOfAcquisition;

    public UsedBook(Book book, LocalDate acquireDate, WayOfAcquisition wayOfAcquisition) {
        this.book = book;
        this.acquireDate = acquireDate;
        this.wayOfAcquisition = wayOfAcquisition;
    }

    public Book getBook() {
        return book;
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    public LocalDate getAcquireDate() {
        return acquireDate;
    }

    public WayOfAcquisition getWayOfAcquisition() {
        return wayOfAcquisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UsedBook) {
            UsedBook usedBook = (UsedBook) o;
            return book.equals(usedBook.book) &&
                    acquireDate.equals(usedBook.acquireDate) &&
                    wayOfAcquisition.equals(usedBook.wayOfAcquisition);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, acquireDate, wayOfAcquisition);
    }

    @Override
    public String toString() {
        return "UsedBook{" +
                "book=" + book +
                ", acquireDate=" + acquireDate +
                ", wayOfAcquisition=" + wayOfAcquisition +
                '}';
    }
}
