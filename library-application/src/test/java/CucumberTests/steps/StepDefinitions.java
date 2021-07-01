package CucumberTests.steps;

import eu.deltasource.library.controllers.BookController;
import eu.deltasource.library.controllers.UserBooksController;
import eu.deltasource.library.entities.Book;
import eu.deltasource.library.entities.EBookInfo;
import eu.deltasource.library.entities.PaperBookInfo;
import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.exceptions.AttemptToDownloadBookWithoutDownloadLinkException;
import eu.deltasource.library.exceptions.PostponementDaysExceedMaximumAllowedException;
import eu.deltasource.library.exceptions.UsedBookNotFoundException;
import eu.deltasource.library.repositories.BookRepository;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.services.SessionService;
import eu.deltasource.library.util.BookFactory;
import eu.deltasource.library.util.UserAccountFactory;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private EBookInfo eBookInfo;
    private SessionService sessionService = SessionService.getInstance();
    private UserAccount userAccount = UserAccountFactory.createUserAccount("Username", "Password");
    private PaperBookInfo paperBookInfo = BookFactory.createPaperBookInfo(1);
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    private BookController bookController = new BookController(bookRepository);
    private UserBooksController userBooksController = new UserBooksController(bookRepository, userRepository);
    private BorrowRequestStatus actualBorrowRequestStatus;
    private String actualExceptionMessage;

    @Before
    public void clearData() {
        bookRepository.clear();
        logOutIfUserIsLoggedIn();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");
        paperBookInfo = BookFactory.createPaperBookInfo(1);
        eBookInfo = BookFactory.createEBookInfo();
    }

    @Given("user logged in")
    public void userLoggedIn() {
        sessionService.logIn(userAccount);
    }

    @When("user tries to borrow book")
    public void userTriesToBorrowBook() {
        actualBorrowRequestStatus = userBooksController.tryToBorrowBook(paperBookInfo);
    }

    @Then("book should be handed to user")
    public void bookShouldBeHandedToUser() {
        BorrowRequestStatus expectedBorrowRequestStatus = BorrowRequestStatus.SUCCESSFUL;
        assertEquals(expectedBorrowRequestStatus, actualBorrowRequestStatus);
    }

    @And("there is available copy")
    public void thereIsAvailableCopy() {
        // no action required here
    }

    @And("there is no available copy")
    public void thereIsNoAvailableCopy() {
        UserAccount dummyUserToBorrowLastBook = UserAccountFactory.createUserAccount("DummyUsername", "Password");
        sessionService.signOut();
        sessionService.logIn(dummyUserToBorrowLastBook);
        userBooksController.tryToBorrowBook(paperBookInfo);
        logOutIfUserIsLoggedIn();
        userLoggedIn();
    }

    @And("borrow request status should be unsuccessful")
    public void borrowRequestStatusShouldBeUnsuccessful() {
        BorrowRequestStatus expectedBorrowRequestStatus = BorrowRequestStatus.IN_QUEUE;
        assertEquals(expectedBorrowRequestStatus, actualBorrowRequestStatus);
    }

    @And("there is a copy locked for the user")
    public void thereIsACopyLockedForTheUser() {
        bookRepository.addNewPaperBook(paperBookInfo, true);
        UserAccount dummyUserToBorrowLastAvailableBook = UserAccountFactory.createUserAccount("DummyUsername", "Password");
        logOutIfUserIsLoggedIn();
        sessionService.logIn(dummyUserToBorrowLastAvailableBook);
        userBooksController.tryToBorrowBook(paperBookInfo);
        logOutIfUserIsLoggedIn();
        userLoggedIn();
        userBooksController.tryToBorrowBook(paperBookInfo);
        logOutIfUserIsLoggedIn();
        sessionService.logIn(dummyUserToBorrowLastAvailableBook);
        userBooksController.returnBookByIsbn("123isbn");
        logOutIfUserIsLoggedIn();
        userLoggedIn();
    }

    @And("user has borrowed book")
    public void userHasBorrowedBook() {
        userBooksController.tryToBorrowBook(paperBookInfo);
    }

    @When("user tries to postpone book")
    public void userTriesToPostponeBook() {
        userBooksController.requestPostponementByIsbn("123isbn", 4);
    }

    @Then("postponement declined")
    public void postponementDeclined() {
        String expectedExceptionMessage = "Postponement days can not exceed 14";
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Then("postponement is successful")
    public void postponementIsSuccessful() {
        int actualPostponedDays = sessionService.getLoggedUser().findBorrowedBookByIsbn("123isbn").get()
                .getPostponeDays();
        int expectedPostponedDays = 4;
        assertEquals(expectedPostponedDays,actualPostponedDays);
    }

    @And("borrowed book is absent")
    public void borrowedBookIsAbsent() {
        //no action required here
    }

    @When("read online book request is made")
    public void readOnlineBookRequestIsMade() {
        bookController.addNewEBook(eBookInfo);
        EBookInfo bookToBeReadOnline = bookController.findEBookByIsbn("123isbn").get();
        userBooksController.readOnlineEBook(bookToBeReadOnline);
    }

    @Then("book should be added to the users used books")
    public void bookShouldBeAddedToTheUsersUsedBooks() {
        Book actualBookFound = userBooksController.findReadOnlineBookByIsbn("123isbn").get().getBook();
        Book expectedBookFound = BookFactory.createEBookInfo().getBook();
        assertEquals(expectedBookFound, actualBookFound);
    }

    @And("e-book with download link is present")
    public void eBookWithDownloadLinkIsPresent() {
        bookController.addNewEBook(BookFactory.createEBookInfo());
    }

    @When("user tries to download book")
    public void userTriesToDownloadBook() {
        EBookInfo bookToBeDownloaded = bookController.findEBookByIsbn("123isbn").get();
        userBooksController.tryToDownloadEBookOnline(bookToBeDownloaded);
    }

    @And("e-book without download link is present")
    public void eBookWithoutDownloadLinkIsPresent() {
        bookController.addNewEBook(BookFactory.createEBookInfoWithoutDownloadLink());
    }

    @Then("download is unsuccessful")
    public void downloadIsUnsuccessful() {
        String expectedMessage = "Can not download book that has no download link.";
        assertTrue(actualExceptionMessage.contains(expectedMessage));
    }

    @When("user tries to postpone not yet borrowed book")
    public void userTriesToPostponeNotYetBorrowedBook() {
        actualExceptionMessage = assertThrows(UsedBookNotFoundException.class,
                () -> userBooksController.requestPostponementByIsbn("123isbn", 15)).getMessage();
    }

    @Then("book should be added to the users downloaded books")
    public void bookShouldBeAddedToTheUsersDownloadedBooks() {
        Book actualDownloadedBookFound = sessionService.getLoggedUser().findDownloadedBookByIsbn("123isbn").get()
                .getBook();
        Book expectedDownloadedBookFound = BookFactory.createEBookInfo().getBook();
        assertEquals(expectedDownloadedBookFound, actualDownloadedBookFound);
    }

    @When("user tries to download book with no download url")
    public void userTriesToDownloadBookWithNoDownloadUrl() {
        actualExceptionMessage = assertThrows(AttemptToDownloadBookWithoutDownloadLinkException.class,
                () -> userBooksController.tryToDownloadEBookOnline(bookController.findEBookByIsbn("123isbn").get())).
                getMessage();
    }

    @When("user tries to postpone book for more than the days allowed")
    public void userTriesToPostponeBookForMoreThanTheDaysAllowed() {
        actualExceptionMessage = assertThrows(PostponementDaysExceedMaximumAllowedException.class,
                () -> userBooksController.requestPostponementByIsbn("123isbn", 15)).getMessage();
    }

    private void logOutIfUserIsLoggedIn() {
        if (sessionService.hasLoggedUser()) {
            sessionService.signOut();
        }
    }
}
