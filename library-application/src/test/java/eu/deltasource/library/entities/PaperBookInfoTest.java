package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.eventSources.DayLapseEventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.util.BookFactory;
import eu.deltasource.library.util.UserAccountFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaperBookInfoTest {

    private PaperBookInfo paperBookInfo;
    private UserAccount userAccountOne;
    private UserAccount userAccountTwo;

    @BeforeEach
    void initializeVariables() {
        paperBookInfo = BookFactory.createPaperBookInfo(1);
        userAccountOne = UserAccountFactory.createUserAccount("Rumen", "Password");
        userAccountTwo = UserAccountFactory.createUserAccount("Georgi", "Password");
    }

    @Test
    void shouldReturnCorrectBorrowRequestDetailsOnEmptyQueueForBorrow() {
        //given
        UserAccount userAccount = UserAccountFactory.createUserAccount("Rumen", "Password");

        //when
        BorrowRequestDetails actualBorrowRequestDetails = paperBookInfo.processBorrowRequest(userAccount);

        //then
        BorrowRequestDetails expectedBorrowRequestDetails = new BorrowRequestDetails(BorrowRequestStatus.SUCCESSFUL, paperBookInfo.getBook());
        assertEquals(expectedBorrowRequestDetails, actualBorrowRequestDetails);
    }

    @Test
    void shouldReturnCorrectBorrowRequestDetailsOnNotEmptyQueueForBorrow() {
        //given
        paperBookInfo.processBorrowRequest(userAccountOne);

        //when
        BorrowRequestDetails actualBorrowRequestDetails = paperBookInfo.processBorrowRequest(userAccountTwo);

        //then
        BorrowRequestDetails expectedBorrowRequestDetails = new BorrowRequestDetails(BorrowRequestStatus.IN_QUEUE,
                28, null);
        assertEquals(expectedBorrowRequestDetails, actualBorrowRequestDetails);
    }

    @Test
    void shouldReturnCorrectBorrowRequestDetailsOnUserAlreadyPresentInQueue() {
        //given
        paperBookInfo.processBorrowRequest(userAccountOne);
        paperBookInfo.processBorrowRequest(userAccountTwo);

        //when
        BorrowRequestDetails actualBorrowRequestDetails = paperBookInfo.processBorrowRequest(userAccountTwo);

        //then
        BorrowRequestDetails expectedBorrowRequestDetails = new BorrowRequestDetails(BorrowRequestStatus.IN_QUEUE,
                28, null);
        assertEquals(expectedBorrowRequestDetails, actualBorrowRequestDetails);
    }

    @Test
    void shouldFreeBookIfThreeDaysHavePassedSinceBookWasLockedForHimAndUserDidntBorrowIt() {
        //given
        paperBookInfo.processBorrowRequest(userAccountOne);
        paperBookInfo.processBorrowRequest(userAccountTwo);
        paperBookInfo.userReturnsBook(userAccountOne);
        DayLapseEventSource dayLapseEventSource = DayLapseEventSource.getInstance();

        //when
        dayLapseEventSource.dayLapsed();
        dayLapseEventSource.dayLapsed();
        dayLapseEventSource.dayLapsed();
        dayLapseEventSource.dayLapsed();

        //then
        int expectedAvailableCopies = 1;
        int actualAvailableCopies = paperBookInfo.getAvailableCopies();
        assertEquals(expectedAvailableCopies, actualAvailableCopies);
    }
}