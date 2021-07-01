package eu.deltasource.library.repositories;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.exceptions.NewBookToAddAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.util.BookFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookJpaRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    public void initializeVariables() {
        bookRepository = new BookRepository();
    }

    @Test
    void shouldReturnCorrectPaperBooksCountOnNewBookAdded() {
        //given
        PaperBookInfo paperBookInfoToBeAddedInRepository = BookFactory.createPaperBookInfo(1);

        //when
        bookRepository.addNewPaperBook(paperBookInfoToBeAddedInRepository, true);

        //then
        long expectedPaperBooksCount = 1;
        long actualPaperBooksCount = bookRepository.getPaperBooksCount();
        assertEquals(expectedPaperBooksCount, actualPaperBooksCount);
    }

    @Test
    void shouldReturnCorrectEBooksCountOnNewBookAdded() {
        //given
        EBookInfo eBookInfoToBeAddedInRepository = BookFactory.createEBookInfo();

        //when
        bookRepository.addNewEBook(eBookInfoToBeAddedInRepository, true);

        //then
        long expectedPaperBooksCount = 1;
        long actualPaperBooksCount = bookRepository.getEBooksCount();
        assertEquals(expectedPaperBooksCount, actualPaperBooksCount);
    }

    @Test
    void shouldReturnCorrectPaperBookOnIsbnSearch() {
        //given
        PaperBookInfo paperBookInfoToBeAddedInRepository = BookFactory.createPaperBookInfo(1);
        bookRepository.addNewPaperBook(paperBookInfoToBeAddedInRepository, true);

        //when
        Optional<PaperBookInfo> actualBookFound = bookRepository.findPaperBookByIsbn("123isbn");

        //then
        assertTrue(actualBookFound.isPresent());
    }

    @Test
    void shouldReturnCorrectEBookOnIsbnSearch() {
        //given
        EBookInfo eBookInfoToBeAddedInRepository = BookFactory.createEBookInfo();
        bookRepository.addNewEBook(eBookInfoToBeAddedInRepository, true);

        //when
        Optional<EBookInfo> actualBookFound = bookRepository.findEBookByIsbn("123isbn");

        //then
        assertTrue(actualBookFound.isPresent());
    }

    @Test
    void shouldThrowExceptionOnAddNewEBookWithAlreadyExistingIsbn() {
        //given
        EBookInfo eBookInfoToBeAddedInRepository = BookFactory.createEBookInfo();
        bookRepository.addNewEBook(eBookInfoToBeAddedInRepository, true);

        //when
        Exception actualExceptionThrown = assertThrows(NewBookToAddAlreadyExistsException.class,
                () -> bookRepository.addNewEBook(eBookInfoToBeAddedInRepository, true));

        //then
        String expectedExceptionMessage = "eu.deltasource.library.entities.Book with same isbn already exists in repository.";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }
}