package eu.deltasource.library.entities;

import eu.deltasource.library.entities.enums.Genre;
import eu.deltasource.library.entities.enums.Categories;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Contains information about a book that will be represented as {@link EBookInfo} or {@link PaperBookInfo}.
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Author> authors;
    private Genre genre;

    @Column(columnDefinition = "TEXT")
    private String shortSummary;
    private String isbn;

    @ElementCollection
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Categories> categories;
    private String imgUrl = "https://images-na.ssl-images-amazon.com/images/I/41+2DiWeWAS._SX345_BO1,204,203,200_.jpg";

    @Builder
    public Book(String title, Set<Author> authors, Genre genre, String shortSummary, String isbn, Set<Categories> categories) {
        setTitle(title);
        setAuthors(authors);
        setGenre(genre);
        setShortSummary(shortSummary);
        setIsbn(isbn);
        setCategories(categories);
    }

    public Book(String title, Set<Author> authors, Genre genre, String shortSummary, String imgUrl, String isbn, Set<Categories> categories) {
        setTitle(title);
        setAuthors(authors);
        setGenre(genre);
        setShortSummary(shortSummary);
        this.imgUrl = imgUrl;
        setIsbn(isbn);
        setCategories(categories);
    }

    public Book(String title, Set<Author> authors, String shortSummary, String imgUrl, String isbn) {
        setTitle(title);
        setAuthors(authors);
        setShortSummary(shortSummary);
        this.imgUrl = imgUrl;
        setIsbn(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Book) {
            Book book = (Book) o;
            return isbn.equals(book.isbn);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
