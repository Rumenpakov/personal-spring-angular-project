package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.exceptions.UserNotLoggedException;
import eu.deltasource.library.repositories.BookRepository;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.services.SessionService;
import eu.deltasource.library.util.BookFactory;
import eu.deltasource.library.util.UserAccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserBooksControllerTest {

    private UserRepository userRepository;
    private UserController userController;
    private BookRepository bookRepository;
    private BookController bookController;
    private UserBooksController userBooksController;

    @BeforeEach
    public void initializeVariables() {
        logOutIfSessionIsActive();
//        userRepository = new UserRepository();
        userController = new UserController(userRepository);
        bookRepository = new BookRepository();
        bookController = new BookController(bookRepository);
        userBooksController = new UserBooksController(bookRepository, userRepository);
    }

    @Test
    void shouldThrowExceptionOnCallingMethodsWithoutLoggedIn() {
        // given
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoToBeBorrowed = BookFactory.createPaperBookInfo(1);

        // when
        Exception actualExceptionThrown = assertThrows(UserNotLoggedException.class, () -> userBooksController.tryToBorrowBook(paperBookInfoToBeBorrowed));

        // then
        String expectedExceptionMessage = "User can not perform operations if not logged";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldReturnSuccessfulBorrowRequestStatusOnUserLoggedInAndBookAvailableToBorrow() {
        // given
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoAddedToBookRepository = BookFactory.createPaperBookInfo(1);
        bookController.addNewPaperBook(paperBookInfoAddedToBookRepository);
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory
                .createUserAccount("Rumen", "Deltasource");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);
        SessionService.getInstance().logIn(userAccountToBeAddedToRepository);
        PaperBookInfo paperBookInfoToTryToBorrow = bookController.findPaperBookByIsbn("123isbn").get();

        // when
        BorrowRequestStatus actualBorrowRequestStatus = userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow);

        // when
        BorrowRequestStatus expectedBorrowRequestStatus = BorrowRequestStatus.SUCCESSFUL;
        assertEquals(actualBorrowRequestStatus, expectedBorrowRequestStatus);
    }

    @Test
    void shouldReturnInQueueBorrowRequestStatusOnUserLoggedInAndBookIsNotAvailableToBorrow() {
        // given
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoAddedToBookRepository = BookFactory.createPaperBookInfo(1);
        bookController.addNewPaperBook(paperBookInfoAddedToBookRepository);
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen",
                "Deltasource");
        UserAccount userAccountToBorrowBookSoThereAreNoAvailableCopies = UserAccountFactory
                .createUserAccount("Georgi", "Deltasource");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);
        userController.addNewUserAccount(userAccountToBorrowBookSoThereAreNoAvailableCopies);
        Optional<PaperBookInfo> paperBookInfoToTryToBorrow = bookController.findPaperBookByIsbn("123isbn");
        assertTrue(paperBookInfoToTryToBorrow.isPresent());
        SessionService.getInstance().logIn(userAccountToBorrowBookSoThereAreNoAvailableCopies);
        userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());
        SessionService.getInstance().signOut();
        SessionService.getInstance().logIn(userAccountToBeAddedToRepository);

        // when
        BorrowRequestStatus actualBorrowRequestStatus = userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());

        // when
        BorrowRequestStatus expectedBorrowRequestStatus = BorrowRequestStatus.IN_QUEUE;
        assertEquals(actualBorrowRequestStatus, expectedBorrowRequestStatus);
    }

    @Test
    void shouldSuccessfullyAddNewBorrowedBookToUserHistory() {
        // given
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoAddedToBookRepository = BookFactory.createPaperBookInfo(1);
        bookController.addNewPaperBook(paperBookInfoAddedToBookRepository);
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen",
                "Deltasource");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);
        SessionService.getInstance().logIn(userAccountToBeAddedToRepository);
        Optional<PaperBookInfo> paperBookInfoToTryToBorrow = bookController.findPaperBookByIsbn("123isbn");
        assertTrue(paperBookInfoToTryToBorrow.isPresent());

        // when
        userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());

        // then
        Optional<BorrowedBook> actualBorrowedBookFound = userBooksController.findBorrowedBookByIsbn("123isbn");
        assertTrue(actualBorrowedBookFound.isPresent());
    }

    @Test
    void shouldNotAddToUsedBooksWhenBorrowRequestStatusIsInQueue() {
        // given
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoAddedToBookRepository = BookFactory.createPaperBookInfo(1);
        bookController.addNewPaperBook(paperBookInfoAddedToBookRepository);
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen",
                "Deltasource");
        UserAccount userAccountToBorrowBookSoThereAreNoAvailableCopies = UserAccountFactory
                .createUserAccount("Georgi", "Deltasource");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);
        userController.addNewUserAccount(userAccountToBorrowBookSoThereAreNoAvailableCopies);
        Optional<PaperBookInfo> paperBookInfoToTryToBorrow = bookController.findPaperBookByIsbn("123isbn");
        assertTrue(paperBookInfoToTryToBorrow.isPresent());
        SessionService.getInstance().logIn(userAccountToBorrowBookSoThereAreNoAvailableCopies);
        userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());
        SessionService.getInstance().signOut();
        SessionService.getInstance().logIn(userAccountToBeAddedToRepository);

        // when
        userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());

        // then
        assertEquals(Optional.empty(), userBooksController.findBorrowedBookByIsbn("123isb"));
    }

    @Test
    void shouldSuccessfullyAddToUsedBooksWhenReadOnlineBook() {
        // given
        logOutIfSessionIsActive();
        EBookInfo eBookInfoToAddToRepository = BookFactory.createEBookInfo();
        bookController.addNewEBook(eBookInfoToAddToRepository);
        UserAccount userAccountToAddToRepository = UserAccountFactory.createUserAccount("Rumen", "Deltasource");
        userController.addNewUserAccount(userAccountToAddToRepository);
        SessionService.getInstance().logIn(userAccountToAddToRepository);
        Optional<EBookInfo> eBookInfoToReadOnline = bookController.findEBookByIsbn("123isbn");
        assertTrue(eBookInfoToReadOnline.isPresent());

        // when
        userBooksController.readOnlineEBook(eBookInfoToReadOnline.get());

        // then
        Optional<UsedBook> actualUsedBookFoundInHistory = userBooksController.findReadOnlineBookByIsbn("123isbn");
        assertTrue(actualUsedBookFoundInHistory.isPresent());
    }

    @Test
    void shouldSuccessfullyAddToUsedBooksWhenDownloadBook() {
        // given
        logOutIfSessionIsActive();
        EBookInfo eBookInfoToAddToRepository = BookFactory.createEBookInfo();
        bookController.addNewEBook(eBookInfoToAddToRepository);
        UserAccount userAccountToAddToRepository = UserAccountFactory.createUserAccount("Rumen", "Deltasource");
        userController.addNewUserAccount(userAccountToAddToRepository);
        SessionService.getInstance().logIn(userAccountToAddToRepository);
        Optional<EBookInfo> eBookInfoToDownload = bookController.findEBookByIsbn("123isbn");
        assertTrue(eBookInfoToDownload.isPresent());

        // when
        userBooksController.tryToDownloadEBookOnline(eBookInfoToDownload.get());

        // then
        Optional<UsedBook> actualUsedBookFoundInHistory = userBooksController.findDownloadedBookByIsbn("123isbn");
        assertTrue(actualUsedBookFoundInHistory.isPresent());
    }

    @Test
    void shouldCorrectlyUpdatePaperBookInfoBorrowedCopies() {
        // given§
        logOutIfSessionIsActive();
        PaperBookInfo paperBookInfoAddedToBookRepository = BookFactory.createPaperBookInfo(1);
        bookController.addNewPaperBook(paperBookInfoAddedToBookRepository);
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen", "Deltasource");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);
        SessionService.getInstance().logIn(userAccountToBeAddedToRepository);
        Optional<PaperBookInfo> paperBookInfoToTryToBorrow = bookController.findPaperBookByIsbn("123isbn");
        assertTrue(paperBookInfoToTryToBorrow.isPresent());
        userBooksController.tryToBorrowBook(paperBookInfoToTryToBorrow.get());

        // when
        userBooksController.returnBookByIsbn("123isbn");

        // when
        int expectedPaperBookAvailableCopies = 1;
        int actualPaperBookAvailableCopies = bookController.findPaperBookByIsbn("123isbn").get().getAvailableCopies();
        assertEquals(expectedPaperBookAvailableCopies, actualPaperBookAvailableCopies);
    }

    private void logOutIfSessionIsActive() {
        if (SessionService.getInstance().hasLoggedUser()) {
            SessionService.getInstance().signOut();
        }
    }
}