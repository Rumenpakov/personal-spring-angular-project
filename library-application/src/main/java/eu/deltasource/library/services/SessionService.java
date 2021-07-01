package eu.deltasource.library.services;

import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.exceptions.LogInWhileActiveSessionIsPresentException;
import eu.deltasource.library.exceptions.LogOutWithoutLoggedInUserException;

/**
 * This class is responsible for keeping track of which user is logged in.
 */
public class SessionService {

    private static final SessionService INSTANCE = new SessionService();
    private UserAccount loggedInUserAccount;

    private SessionService() {
    }

    /**
     * Gets singleton instance of {@link SessionService}.
     *
     * @return Instance of {@link SessionService}
     */
    public static SessionService getInstance() {
        return INSTANCE;
    }



    /**
     * Called when user has successfully logged in.
     *
     * @param userAccount User account to be saved as currently logged.
     */
    public void logIn(UserAccount userAccount) {
        if (hasLoggedUser()) {
            throw new LogInWhileActiveSessionIsPresentException("Can not log in while there is active session.");
        }
        loggedInUserAccount = userAccount;
    }

    /**
     * Called when user logs out.
     */
    public void signOut() {
        if (!hasLoggedUser()) {
            throw new LogOutWithoutLoggedInUserException("Unable to log out without active log in session.");
        }
        loggedInUserAccount = null;
    }

    /**
     * Checks if user is currently logged in.
     *
     * @return True if user is logged.
     */
    public boolean hasLoggedUser() {
        return loggedInUserAccount != null;
    }

    public UserAccount getLoggedUser() {
        return loggedInUserAccount;
    }
}
