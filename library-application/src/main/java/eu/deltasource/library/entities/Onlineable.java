package eu.deltasource.library.entities;

/**
 * Contains methods necessary for accessing book online.
 */
public interface Onlineable {

    boolean isDownloadable();

    String getDownloadLink();

    String getReadOnlineLink();

    Book getBook();
}
