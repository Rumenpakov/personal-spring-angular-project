package eu.deltasource.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.deltasource.library.exceptions.IllegalInputException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains all necessary information for user account.
 * Also provides methods for manipulating its history and retrieving necessary information.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private UserAccountDetails userAccountDetails;
    @OneToOne
    private UserAccountHistory userAccountHistory = new UserAccountHistory();
    @ElementCollection
    @CollectionTable(name = "notifications", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private List<NotificationType> notifications = new ArrayList<>();

    public UserAccount(UserAccountDetails userAccountDetails) {
        setUserAccountDetails(userAccountDetails);
    }

    private void setUserAccountDetails(UserAccountDetails userAccountDetails) {
        if (userAccountDetails == null) {
            throw new IllegalInputException("User account details can not be null");
        }
        this.userAccountDetails = userAccountDetails;
    }

    @JsonIgnore
    public String getUserName() {
        return userAccountDetails.getUserName();
    }

    /**
     * If given password matches with the password of this account.
     *
     * @param password Given password
     * @return True if passwords match
     */
    public boolean isPasswordMatching(String password) {
        return userAccountDetails.isPasswordMatching(password);
    }

    /**
     * Adds used book to user history.
     *
     * @param usedBook Used book to add to history
     */
    public void addUsedBook(UsedBook usedBook) {
        userAccountHistory.addUsedBook(usedBook);
    }

    /**
     * Searches for borrowed book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found borrowed book
     */
    public Optional<BorrowedBook> findBorrowedBookByIsbn(String isbn) {
        return userAccountHistory.findBorrowedBookByIsbn(isbn);
    }

    /**
     * Searches for read online book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found used book
     */
    public Optional<UsedBook> findReadOnlineBookByIsbn(String isbn) {
        return userAccountHistory.findReadOnlineBookByIsbn(isbn);
    }

    /**
     * Searches for downloaded book by isbn.
     *
     * @param isbn Isbn to serach by
     * @return Found used book
     */
    public Optional<UsedBook> findDownloadedBookByIsbn(String isbn) {
        return userAccountHistory.findDownloadedBookByIsbn(isbn);
    }

    /**
     * Adds notification to the list of notifications about this user.
     *
     * @param notificationType
     */
    public void notify(NotificationType notificationType) {
        notifications.add(notificationType);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userAccountDetails=" + userAccountDetails +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserAccount) {
            UserAccount that = (UserAccount) o;
            return this.userAccountDetails.equals(that.userAccountDetails);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAccountDetails);
    }
}

