package eu.deltasource.library.exceptions;

public class UriCreationException extends RuntimeException {

    public UriCreationException() {
        super("Uri could not be created");
    }
}
