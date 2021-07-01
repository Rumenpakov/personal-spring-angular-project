package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.WayOfAcquisition;
import eu.deltasource.library.exceptions.IllegalUsedBookException;
import eu.deltasource.library.exceptions.UsedBookNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * This class contains the history of a user, such as borrowed books, read online books and downloaded online books.
 * Methods for finding used books are exposed, as well as methods for adding more used books ot the history.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserAccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UsedBook> usedBooks = new HashSet<>();

    /**
     * Adds used book to user history.
     *
     * @param usedBook Used book to add to history
     */
    public void addUsedBook(UsedBook usedBook) {
        if (usedBooks.contains(usedBook)) {
            throw new IllegalUsedBookException("Used book is already in used books history");
        }
        usedBooks.add(usedBook);
    }

    /**
     * Searches for borrowed book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found borrowed book
     */
    public Optional<BorrowedBook> findBorrowedBookByIsbn(String isbn) {
        return usedBooks.stream()
                .filter(usedBook -> usedBook.getWayOfAcquisition() == WayOfAcquisition.BORROW)
                .filter(usedBook -> usedBook.getIsbn().equalsIgnoreCase(isbn))
                .findFirst()
                .map(usedBook -> (BorrowedBook) usedBook);
    }

    /**
     * Searches for read online book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found used book
     * @throws UsedBookNotFoundException when the book that is searched is not found
     */
    public Optional<UsedBook> findReadOnlineBookByIsbn(String isbn) {
        return usedBooks.stream()
                .filter(usedBook -> isTheBookWeLookFor(isbn, WayOfAcquisition.READ_ONLINE, usedBook))
                .findFirst();
    }

    /**
     * Check if a given book is the one that is being searched for.
     *
     * @param isbn Isbn for book that we search for
     * @param usedBook Given book
     * @return True if the book is the one that we search for
     */
    private boolean isTheBookWeLookFor(String isbn,WayOfAcquisition wayOfAcquisitionCriteria, UsedBook usedBook) {
        return usedBook.getWayOfAcquisition() == wayOfAcquisitionCriteria
                && usedBook.getIsbn().equalsIgnoreCase(isbn);
    }

    /**
     * Searches for downloaded book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found used book
     * @throws UsedBookNotFoundException when the book that is searched is not found
     */
    public Optional<UsedBook> findDownloadedBookByIsbn(String isbn) {
        return usedBooks.stream()
                .filter(usedBook -> isTheBookWeLookFor(isbn, WayOfAcquisition.DOWNLOAD, usedBook))
                .findFirst();
    }
}
