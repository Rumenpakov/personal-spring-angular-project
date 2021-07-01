package eu.deltasource.library.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalLogInCredentialException extends RuntimeException {

    public IllegalLogInCredentialException(String s) {
        super(s);
    }
}
