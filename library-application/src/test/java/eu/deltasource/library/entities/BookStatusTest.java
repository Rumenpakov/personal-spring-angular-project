package eu.deltasource.library.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.util.UserAccountFactory;

import static org.junit.jupiter.api.Assertions.*;

class BookStatusTest {

    private BookStatus bookStatus;
    private UserAccount userAccount;

    @BeforeEach
    public void initializeVariables() {
        bookStatus = new BookStatus();
        userAccount = UserAccountFactory.createUserAccount("Rumen", "Password");
    }

    @Test
    void shouldNotBeAvailableIfLockedForUser() {
        //given
        bookStatus.lockBookForUser(userAccount);

        //when
        boolean actualValue = bookStatus.isAvailable();

        //then
        assertFalse(actualValue);
    }

    @Test
    void shouldNotBeAvailableIfBorrowedByUser() {
        //given
        bookStatus.setBorrowerDetails(userAccount);

        //when
        boolean actualValue = bookStatus.isAvailable();

        //then
        assertFalse(actualValue);
    }

    @Test
    void shouldBeAvailableIfNotLockedAndNotBorrowed() {
        //given

        //when
        boolean actualValue = bookStatus.isAvailable();

        //then
        assertTrue(actualValue);
    }

    @Test
    void shouldBeAvailableIfReturnedAfterBeingBorrowed() {
        //given
        bookStatus.setBorrowerDetails(userAccount);
        bookStatus.onBookReturnedByUser();

        //when
        boolean actualValue = bookStatus.isAvailable();

        //then
        assertTrue(actualValue);
    }

    @Test
    void shouldBeAvailableIfLockedButThenFreed() {
        //given
        bookStatus.setBorrowerDetails(userAccount);
        bookStatus.freeBook();

        //when
        boolean actualValue = bookStatus.isAvailable();

        //then
        assertTrue(actualValue);
    }
}