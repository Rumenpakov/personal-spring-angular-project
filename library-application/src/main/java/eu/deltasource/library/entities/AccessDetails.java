package eu.deltasource.library.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Contains urls for access of {@link EBookInfo} online.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class AccessDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String readOnlineUrl;
    private String downloadUrl;

    public AccessDetails(String readOnlineUrl, String downloadUrl) {
        setReadOnlineUrl(readOnlineUrl);
        setDownloadUrl(downloadUrl);
    }

    public boolean hasDownloadUrl() {
        return downloadUrl != null;
    }

    public void setDownloadUrl(String downloadUrl) {
        if (downloadUrl != null) {
            downloadUrl = downloadUrl.trim();
        }
        this.downloadUrl = downloadUrl;
    }
}
