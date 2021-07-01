package eu.deltasource.library.util;

import eu.deltasource.library.exceptions.UriCreationException;

import java.net.URI;
import java.net.URISyntaxException;

public class Paths {
    public static final String BOOKS = "/books";
    public static final String USERS = "/users";

    public static URI LocationBuilder(String root, String target) {
        try {
            return new URI(root + "/" + target);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new UriCreationException();
        }
    }
}
