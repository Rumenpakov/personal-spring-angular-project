package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.enums.BorrowRequestStatus;
import eu.deltasource.library.entities.enums.WayOfAcquisition;
import eu.deltasource.library.exceptions.*;
import eu.deltasource.library.repositories.BookRepository;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Optional;

import static eu.deltasource.library.entities.BorrowRequestDetails.isBorrowSuccessful;

/**
 * Controller for operations executed from user to the books, such as borrowing, returning, and reading online.
 * Methods for searching books are also present.
 */
@RestController
@RequestMapping(path = "/user-books")
public class UserBooksController {

    private SessionService sessionService = SessionService.getInstance();
    private UserRepository userRepository;
    private BookRepository bookRepository;

    public UserBooksController(BookRepository bookRepository, UserRepository userRepository) {
        setBookRepository(bookRepository);
        setUserRepository(userRepository);
    }

    private void setUserRepository(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalInputException("Book repository for user users controller can not be null");
        }
        this.userRepository = userRepository;
    }

    private void setBookRepository(BookRepository bookRepository) {
        if (bookRepository == null) {
            throw new IllegalInputException("Book repository for user books controller can not be null");
        }
        this.bookRepository = bookRepository;
    }

    /**
     * Sends a request to borrow book. Request is deemed successful based on current state of the book to borrow.
     * If request is successful, {@link BorrowedBook} is added to user account history.
     *
     * @param book Book to try to borrow
     * @return Status of request
     */
    public BorrowRequestStatus tryToBorrowBook(Borrowable book) {
        if (book == null) {
            throw new IllegalInputException("Book to try to borrow can not be null");
        }
        verifyLoggedUser();
        BorrowRequestDetails borrowRequestDetails = book.processBorrowRequest(sessionService.getLoggedUser());
        if (isBorrowSuccessful(borrowRequestDetails)) {
            BorrowedBook borrowedBook = new BorrowedBook(borrowRequestDetails.getBook(), LocalDate.now());
            sessionService.getLoggedUser().addUsedBook(borrowedBook);
        }
        return borrowRequestDetails.getBorrowRequestStatus();
    }

    /**
     * Reads online E-book and stores it as used book in user history.
     *
     * @param book Book to read online
     */
    public UsedBook readOnlineEBook(Onlineable book) {
        if (book == null) {
            throw new IllegalInputException("Book to try to read online can not be null");
        }
        verifyLoggedUser();
        UsedBook usedBook = new UsedBook(book.getBook(), LocalDate.now(), WayOfAcquisition.READ_ONLINE);
        sessionService.getLoggedUser().addUsedBook(usedBook);
        return usedBook;
    }

    /**
     * Tries to download E-book if such download link is available.
     *
     * @param book Book to download online
     * @throws IllegalInputException        if book to download online is null
     * @throws IllegalDownloadRequestException if book to download doesnt have downlaod url
     */
    public UsedBook tryToDownloadEBookOnline(Onlineable book) {
        if (book == null) {
            throw new IllegalInputException("Book to download online can not be null");
        }
        if (!book.isDownloadable()) {
            throw new IllegalDownloadRequestException("Book to download is not available for download online." +
                    " Value passed: " + book);
        }

        verifyLoggedUser();
        UsedBook usedBook = new UsedBook(book.getBook(), LocalDate.now(), WayOfAcquisition.DOWNLOAD);
        sessionService.getLoggedUser().addUsedBook(usedBook);
        return usedBook;
    }

    /**
     * Searches for borrowed book by given isbn.
     *
     * @param isbn Isbn to search by
     * @return Found borrowed book
     */
    public Optional<BorrowedBook> findBorrowedBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalInputException("Isbn to find borrowed book by can not be null or empty");
        }

        return sessionService.getLoggedUser().findBorrowedBookByIsbn(isbn);
    }

    /**
     * Searches for read online book by given isbn.
     *
     * @param isbn Isbn to search by
     * @return Found read online book
     */
    public Optional<UsedBook> findReadOnlineBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalInputException("Isbn to find read online book by can not be null or empty");
        }

        return sessionService.getLoggedUser().findReadOnlineBookByIsbn(isbn);
    }

    /**
     * Searches for downloaded book by given isbn.
     *
     * @param isbn Isbn to search by
     * @return Found downloaded book
     */
    public Optional<UsedBook> findDownloadedBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalInputException("Isbn to find read online book by can not be null or empty");
        }

        return sessionService.getLoggedUser().findDownloadedBookByIsbn(isbn);
    }

    /**
     * Returns already borrowed {@link PaperBookInfo}, as well as settings its return date to today.
     *
     * @param isbn
     */
    public BorrowedBook returnBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalInputException("Isbn of book to return can not be null");
        }
        verifyLoggedUser();
        BorrowedBook borrowedBook = getBorrowedBook(isbn);
        if (borrowedBook.getReturnDate().isPresent()) {
            throw new BookAlreadyReturnedException("Cant return already returned book");
        }
        borrowedBook.setReturnDate(LocalDate.now());
        PaperBookInfo paperBookInfoOfBookToReturn = getPaperBookInfo(borrowedBook);
        paperBookInfoOfBookToReturn.userReturnsBook(sessionService.getLoggedUser());
        return borrowedBook;
    }

    /**
     * Retrieves logged user account.
     *
     * @return User account if such is logged in.
     * @throws UserNotLoggedException when an operation is attempted to be made without logged in user
     */
    public void requestPostponementByIsbn(String isbn, int daysToPostpone) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalInputException("Isbn of book to postpone can not be null or empty");
        }
        verifyLoggedUser();
        BorrowedBook borrowedBook = getBorrowedBook(isbn);
        processPostponementRequest(daysToPostpone, borrowedBook);
    }

    /**
     * Validates the postponement request, such as checking if the requested days falls in the allowed period
     * of postponement
     *
     * @param daysToPostpone Requested days to postpone
     * @param borrowedBook Book to postpone the return of
     */
    private void processPostponementRequest(int daysToPostpone, BorrowedBook borrowedBook) {
        if (borrowedBook.getPostponeDays() + daysToPostpone > 14) {
            throw new PostponementDaysExceedMaximumAllowedException("Postponement days can not exceed 14. Value passed: "
                    + daysToPostpone);
        }
        borrowedBook.addToPostponedDays(daysToPostpone);
    }

    /**
     * This method searches for {@link PaperBookInfo} in {@link BookRepository} based on previously borrowed book.
     *
     * @param borrowedBook The previously borrowed book
     * @return Found paper book
     */
    private PaperBookInfo getPaperBookInfo(BorrowedBook borrowedBook) {
        Optional<PaperBookInfo> paperBookInfoOfBookToReturn = bookRepository.findPaperBookByIsbn(borrowedBook.getIsbn());
        if (!paperBookInfoOfBookToReturn.isPresent()) {
            throw new BookNotFoundException("Paper book not found");
        }
        return paperBookInfoOfBookToReturn.get();
    }

    /**
     * This method searches for borrowed book in {@link UserAccountHistory}.
     *
     * @param isbn Isbn of the book to search for
     * @return Found borrowed book
     */
    private BorrowedBook getBorrowedBook(String isbn) {
        Optional<BorrowedBook> borrowedBook = sessionService.getLoggedUser().findBorrowedBookByIsbn(isbn);
        if (!borrowedBook.isPresent()) {
            throw new BookNotFoundException("User does not currently hold this book as borrowed");
        }
        return borrowedBook.get();
    }

    /**
     * Verifies if user is logged in before performing operations that require user rights.
     */
    private void verifyLoggedUser() {
        if (sessionService.getLoggedUser() == null) {
            throw new UserNotLoggedException("User can not perform operations if not logged");
        }
    }

    @GetMapping("/read-online/{isbn}")
    public ResponseEntity<UsedBook> readOnlineEBookByIsbn(@PathVariable(value = "isbn") String isbn) {
        Optional<EBookInfo> foundEBookInfo = bookRepository.findEBookByIsbn(isbn);
        if (!foundEBookInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(readOnlineEBook(foundEBookInfo.get()), HttpStatus.OK);
    }

    @GetMapping("/borrow-book/{isbn}")
    public ResponseEntity<BorrowRequestStatus> tryToBorrowBookByIsbn(@PathVariable(value = "isbn") String isbn) {
        Optional<PaperBookInfo> foundPaperBookInfo = bookRepository.findPaperBookByIsbn(isbn);
        if (!foundPaperBookInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tryToBorrowBook(foundPaperBookInfo.get()), HttpStatus.OK);
    }

    @GetMapping("/download/{isbn}")
    public ResponseEntity<UsedBook> downloadEBookByIsbn(@PathVariable(value = "isbn") String isbn) {
        Optional<EBookInfo> foundEBookInfo = bookRepository.findEBookByIsbn(isbn);
        if (!foundEBookInfo.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tryToDownloadEBookOnline(foundEBookInfo.get()), HttpStatus.OK);
    }

    @GetMapping("/find-borrowed/{isbn}")
    public ResponseEntity<UsedBook> findBorrowedBookByIsbnController(@PathVariable(value = "isbn") String isbn, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserAccount loggedUser = getUserFromRequestSession(session);
        Optional<BorrowedBook> foundBorrowedBook = findBorrowedBookByIsbn(isbn);
        if (!foundBorrowedBook.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(foundBorrowedBook.get(), HttpStatus.OK);
    }

    private UserAccount getUserFromRequestSession(HttpSession session) {
        String userName = session.getAttribute("userName").toString();
        Optional<UserAccount> foundUser = userRepository.findAccountByUsername(userName);
        if (foundUser.isPresent()) {
            return  foundUser.get();
        }
        throw new NotAuthorizedException("Can not perform this action without being logged in.");
    }

    @GetMapping("/return/{isbn}")
    public ResponseEntity<BorrowedBook> returnBookByIsbnController(@PathVariable(value = "isbn") String isbn) {
        return new ResponseEntity<>(returnBookByIsbn(isbn), HttpStatus.OK);
    }


}



