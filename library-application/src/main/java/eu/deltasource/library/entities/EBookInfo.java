package eu.deltasource.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.deltasource.library.exceptions.AttemptToDownloadBookWithoutDownloadLinkException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Contains all necessary information about E-book, such as access links for reading and downlaoding online.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity

public class EBookInfo implements Onlineable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private AccessDetails accessDetails;
    @OneToOne
    private Book book;

    public EBookInfo(AccessDetails accessDetails, Book book) {
        setAccessDetails(accessDetails);
        setBook(book);
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    /**
     * Checks if book has link to be downloaded online.
     *
     * @return True if download link is present
     */
    @Override
    public boolean isDownloadable() {
        if (accessDetails.hasDownloadUrl()) {
            return !accessDetails.getDownloadUrl().isEmpty();
        }
        throw new AttemptToDownloadBookWithoutDownloadLinkException("Can not download book that has no download link.");
    }

    /**
     * Retrieves download link.
     *
     * @return Download link
     */
    @Override
    public String getDownloadLink() {
        if (accessDetails.hasDownloadUrl()) {
            return accessDetails.getDownloadUrl();
        }
        throw new AttemptToDownloadBookWithoutDownloadLinkException("Can not download book that has no download link");
    }

    /**
     * Retrieves link to read E-book online.
     *
     * @return Link for readling online
     */
    @Override
    public String getReadOnlineLink() {
        return accessDetails.getReadOnlineUrl();
    }
}
