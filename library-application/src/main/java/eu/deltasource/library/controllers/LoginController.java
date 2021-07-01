package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.entities.models.LogInModel;
import eu.deltasource.library.entities.models.SignUpModel;
import eu.deltasource.library.exceptions.*;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.services.AuthService;
import eu.deltasource.library.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * This class is used to log in and log out of user account.
 * In case user tries to log in while another user is currently active,
 * an {@link LogInWhileActiveSessionIsPresentException} exception is thrown.
 * In case user tries to log out while no session is active,
 * an {@link LogOutWithoutLoggedInUserException} is thrown.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/authh")
public class LoginController {

    @Autowired
    private AuthService authService;

    private SessionService sessionService = SessionService.getInstance();
    private UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        setUserRepository(userRepository);
    }

    private void setUserRepository(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalInputException("User repository can not be null");
        }
        this.userRepository = userRepository;
    }

    @PostMapping("signup")
    public void signup(@RequestBody SignUpModel signUpModel) {
        authService.signUp(signUpModel);
    }

    /**
     * Logs into user account by given username and password
     *
     * @param username Username of the account to log into
     * @param password Password of the account to log into
     * @return User account if log in credentials are correct
     * @throws IncorrectPasswordException when password doesnt match
     */
    public UserAccount logIn(String username, String password) {
        logInCredentialsNullCheck(username, password);
        UserAccount foundUserAccount = getUserAccount(username);
        if (isPasswordMatching(password, foundUserAccount)) {
            sessionService.logIn(foundUserAccount);
            return foundUserAccount;
        }
        throw new IncorrectPasswordException("Entered password doesn't match account password. Entered password: " + password);
    }

    private UserAccount getUserAccount(String username) {
        Optional<UserAccount> foundUserAccount = userRepository.findAccountByUsername(username);
        if (!foundUserAccount.isPresent()) {
            throw new UserAccountNotFoundException("User account couldn't be found");
        }
        return foundUserAccount.get();
    }

    /**
     * Performs null or empty check on credentials
     *
     * @param username Username to check
     * @param password Password to check
     */
    private void logInCredentialsNullCheck(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalLogInCredentialException("Log in username can not be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalLogInCredentialException("Log in password can not be null or empty");
        }
    }

    /**
     * Checks if given password matches the account to log into.
     *
     * @param password    Password to check
     * @param userAccount Account to log into
     * @return True if passwords match
     */
    private boolean isPasswordMatching(String password, UserAccount userAccount) {
        return userAccount.isPasswordMatching(password);
    }

    /**
     * Checks if user is logged in.
     *
     * @return True if user is logged in
     */
    public boolean hasLoggedUser() {
        return sessionService.hasLoggedUser();
    }

    /**
     * Logs out of currently logged in account
     */
    public void logOut() {
        sessionService.signOut();
    }
}
