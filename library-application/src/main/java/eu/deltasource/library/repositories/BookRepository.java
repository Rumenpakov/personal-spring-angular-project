package eu.deltasource.library.repositories;

import eu.deltasource.library.entities.Author;
import eu.deltasource.library.entities.EBookInfo;
import eu.deltasource.library.entities.PaperBookInfo;
import eu.deltasource.library.exceptions.NewBookToAddAlreadyExistsException;
import eu.deltasource.library.repositories.jpaRepositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Contains all paper books and e-books, and the methods necessary to add remove and find, based on isbn.
 */
@Repository
// TODO req args construct final reps
public class BookRepository {

    @Autowired
    private NameRepository nameRepository;
    @Autowired
    private AuthorRepostiry authorRepostiry;
    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private PaperBookRepository paperBookRepository;
    @Autowired
    private EBookRepository eBookRepository;
    @Autowired
    private AccessDetailsRepository accessDetailsRepository;

    public long getPaperBooksCount() {
        return paperBookRepository.count();
    }

    public long getEBooksCount() {
        return eBookRepository.count();
    }

    /**
     * Searches for paper book by given isbn.
     *
     * @param isbn Isbn to search by.
     * @return Paper book found
     */
    public Optional<PaperBookInfo> findPaperBookByIsbn(String isbn) {
        return paperBookRepository.findByBook_Isbn(isbn);
    }

    /**
     * Fetches all present paper books.
     *
     * @return All paper books
     */
    public Iterable<PaperBookInfo> findAllPaperBooks() {
        return paperBookRepository.findAll();
    }

    /**
     * Fetches all present e-books.
     *
     * @return All e-books
     */
    public Iterable<EBookInfo> findAllEBooks() {
        return eBookRepository.findAll();
    }

    public void addNewPaperBook(PaperBookInfo paperBookInfo, boolean areAuthorsNew) {
        if (isPaperBookPresentInRepository(paperBookInfo.getIsbn())) {
            throw new NewBookToAddAlreadyExistsException("Book with same isbn already exists in repository." +
                    " Value passed: " + paperBookInfo);
        }

        //TODO extract and persist
        //TODO areauthorsnew
        if (areAuthorsNew) {
            paperBookInfo.getBook().getAuthors().forEach(author -> {
                nameRepository.save(author.getName());
                authorRepostiry.save(author);
            });
        }
        bookJpaRepository.save(paperBookInfo.getBook());
        paperBookRepository.save(paperBookInfo);
    }

    /**
     * Checks if paper book exists in repository by given isbn.
     *
     * @param isbn Isbn to search by
     * @return True if present
     */
    private boolean isPaperBookPresentInRepository(String isbn) {
        return findPaperBookByIsbn(isbn).isPresent();
    }

    /**
     * Adds new e-book to repository.
     *
     * @param eBookInfo E-book to add
     */
    public void addNewEBook(EBookInfo eBookInfo, boolean areAuthorsNew) {
        if (isEbookPresentInRepository(eBookInfo.getIsbn())) {
            throw new NewBookToAddAlreadyExistsException("Book with same isbn already exists in repository." +
                    " Value passed: " + eBookInfo);
        }

        if (areAuthorsNew) {
            eBookInfo.getBook().getAuthors().forEach(author -> {
                nameRepository.save(author.getName());
                authorRepostiry.save(author);
            });
        }
        accessDetailsRepository.save(eBookInfo.getAccessDetails());
        bookJpaRepository.save(eBookInfo.getBook());
        eBookRepository.save(eBookInfo);
    }

    /**
     * Checks if E-book exists in repository by given isbn.
     *
     * @param isbn Isbn to search by.
     * @return True if present
     */
    private boolean isEbookPresentInRepository(String isbn) {
        return findEBookByIsbn(isbn).isPresent();
    }

    public Optional<EBookInfo> findEBookByIsbn(String isbn) {
        return eBookRepository.findByBook_Isbn(isbn);
    }

    public void clear() {
        if (paperBookRepository != null && eBookRepository != null) {
            paperBookRepository.deleteAll();
            eBookRepository.deleteAll();
        }
    }
}
