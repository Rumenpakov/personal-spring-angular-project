package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.EBookInfo;
import eu.deltasource.library.entities.PaperBookInfo;
import eu.deltasource.library.exceptions.IllegalInputException;
import eu.deltasource.library.repositories.BookRepository;
import eu.deltasource.library.util.BookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest {

    private BookController bookController;

    @BeforeEach
    public void initialiseBookController() {
        bookController = new BookController(new BookRepository());
    }

    @Test
    void shouldReturnCorrectCountOfPaperBooks() {
        //given
        PaperBookInfo paperBookInfoToAddToRepository = BookFactory.createPaperBookInfo(1);

        //when
        bookController.addNewPaperBook(paperBookInfoToAddToRepository);

        //then
        long expectedPaperBooksCount = 1;
        long actualPaperBooksCount = bookController.getPaperBooksCount();
        assertEquals(expectedPaperBooksCount, actualPaperBooksCount);
    }

    @Test
    void shouldReturnCorrectCountOfEBooks() {
        //given
        EBookInfo eBookInfoToAddToRepository = BookFactory.createEBookInfo();

        //when
        bookController.addNewEBook(eBookInfoToAddToRepository);

        //then
        long expectedEBooksCount = 1;
        long actualEBooksCount = bookController.getEBooksCount();
        assertEquals(expectedEBooksCount, actualEBooksCount);
    }

    @Test
    void shouldSuccessfullyFindExistingPaperBook() {
        //given
        PaperBookInfo paperBookInfoToAddToRepository = BookFactory.createPaperBookInfo(1);
        String paperBookIsbn = paperBookInfoToAddToRepository.getIsbn();
        bookController.addNewPaperBook(paperBookInfoToAddToRepository);

        //when
        Optional<PaperBookInfo> actualPaperBookFound = bookController.findPaperBookByIsbn(paperBookIsbn);

        //then
        assertTrue(actualPaperBookFound.isPresent());
    }

    @Test
    void shouldSuccessfullyFindExistingEBook() {
        //given
        EBookInfo eBookInfoToAddToRepository = BookFactory.createEBookInfo();
        String eBookIsbn = eBookInfoToAddToRepository.getIsbn();
        bookController.addNewEBook(eBookInfoToAddToRepository);

        //when
        Optional<EBookInfo> actualEBookFound = bookController.findEBookByIsbn(eBookIsbn);

        //then
        assertTrue(actualEBookFound.isPresent());
    }

    @Test
    void shouldThrowCorrectExceptionWhenAddNewPaperBookWithNullParameter() {
        //given

        //when
        Exception actualExceptionThrown = assertThrows(IllegalInputException.class, () -> bookController.addNewPaperBook(null));

        //then
        String expectedExceptionMessage = "Paper book to add can not be null";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldThrowCorrectExceptionWhenAddNewEBookWithNullParamter() {
        //given

        //when
        Exception actualExceptionThrown = assertThrows(IllegalInputException.class, () -> bookController.addNewEBook(null));

        //then
        String expectedExceptionMessage = "E-book to add can not be null";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldThrowCorrectExceptionWhenFindPaperBookByIsbnWithNullParameter() {
        //given

        //when
        Exception actualExceptionThrown = assertThrows(IllegalInputException.class, () -> bookController.findPaperBookByIsbn(null));

        //then
        String expectedExceptionMessage = "Isbn to search paper book by can not be null or empty";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldThrowCorrectExceptionOnNullWhenFindEBookByIsbnWithNullParameter() {
        //given

        //when
        Exception actualExceptionThrown = assertThrows(IllegalInputException.class, () -> bookController.findEBookByIsbn(null));

        //then
        String expectedExceptionMessage = "Isbn to search e-book by can not be null or empty";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }
}