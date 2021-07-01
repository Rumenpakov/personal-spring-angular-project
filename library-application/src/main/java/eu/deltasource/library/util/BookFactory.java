package eu.deltasource.library.util;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.enums.Genre;
import eu.deltasource.library.entities.enums.Categories;
import eu.deltasource.library.repositories.jpaRepositories.AuthorRepostiry;
import eu.deltasource.library.repositories.jpaRepositories.BookJpaRepository;
import eu.deltasource.library.repositories.jpaRepositories.NameRepository;
import eu.deltasource.library.repositories.jpaRepositories.PaperBookRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to create e-books and paper books. Methods for creating e-books with and without download links
 * are present, as well as paper book copies count can be specified.
 */
public class BookFactory {

    @Autowired
    private static NameRepository nameRepository;
    @Autowired
    private static AuthorRepostiry authorRepostiry;
    @Autowired
    private static BookJpaRepository bookJpaRepository;
    @Autowired
    private static PaperBookRepository paperBookRepository;

    /**
     * Creates e-book with all links present.
     *
     * @return created e-book
     */
    public static EBookInfo createEBookInfo() {
        AccessDetails accessDetails = new AccessDetails("ReadOnline.com", "DownloadOnline.com");
        return getEBookInfo(accessDetails);
    }

    /**
     * Creates e-book without download link.
     *
     * @return create e-book
     */
    public static EBookInfo createEBookInfoWithoutDownloadLink() {
        AccessDetails accessDetails = new AccessDetails("ReadOnline.com", null);
        return getEBookInfo(accessDetails);
    }

    /**
     * Creates paper book with given amount of copies.
     *
     * @param copiesCount given amount of copies
     * @return created paper book
     */
    public static PaperBookInfo createPaperBookInfo(int copiesCount) {
        Set<Author> authors = new HashSet<>();
        Name authorName = new Name("Bilbo Baggins");
        LocalDate dateOfBirth = LocalDate.of(2000, Month.APRIL, 23);
        Author author = new Author(authorName, "Bulgaria", dateOfBirth);
        authors.add(author);
        Book book = new Book("The cat thatz scratches itself", authors, Genre.HORROR,
                "Lorem ipsum you know the deal", "123isbn", Collections.singleton(Categories.LOVE));
        return new PaperBookInfo(copiesCount, book);
    }

    /**
     * Creates fine details about e-book.
     *
     * @param accessDetails Access details to assemble the e-book object with
     * @return created e-book
     */
    private static EBookInfo getEBookInfo(AccessDetails accessDetails) {
        Set<Author> authors = new HashSet<>();
        Name authorName = new Name("Bilbo Baggins");
        LocalDate dateOfBirth = LocalDate.of(2000, Month.APRIL, 23);
        Author author = new Author(authorName, "Bulgaria", dateOfBirth);
        authors.add(author);
        Book book = new Book("The cat that scratches itself", authors, Genre.HORROR,
                "Lorem ipsum you know the deal", "123isbn", Collections.singleton(Categories.LOVE));
        return new EBookInfo(accessDetails, book);
    }

    public static PaperBookInfo createPaperBookInfo(String isbn, String title, String shortSummary, String imgUrl, String authorFirstName, Categories... categories) {
        Set<Author> authors = new HashSet<>();
        Name authorName = new Name(authorFirstName);
        LocalDate dateOfBirth = LocalDate.of(2000, Month.APRIL, 23);
        Author author = new Author(authorName, "Bulgaria", dateOfBirth);
        authors.add(author);
        Book book = new Book(title, authors, Genre.HORROR,
                shortSummary, imgUrl, isbn, new HashSet<>(Arrays.asList(categories)));
        return new PaperBookInfo(1, book);
    }
}
