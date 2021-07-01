package eu.deltasource.library.services;

import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.exceptions.LogInWhileActiveSessionIsPresentException;
import eu.deltasource.library.exceptions.LogOutWithoutLoggedInUserException;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.util.UserAccountFactory;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest {
    SessionService sessionService;
    UserAccount userAccount;

    @Test
    void shouldCorrectlyChangeUserIsLoggedInWhenLogIn() {
        // given
        logOutIfSessionIsActive();
        sessionService = SessionService.getInstance();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");

        // when
        sessionService.logIn(userAccount);

        // then
        assertTrue(sessionService.hasLoggedUser());
    }

    @Test
    void shouldCorrectlyChangeUserIsLoggedInWhenLogOut() {
        // given
        logOutIfSessionIsActive();
        sessionService = SessionService.getInstance();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");
        sessionService.logIn(userAccount);

        // when
        sessionService.signOut();

        // then
        assertFalse(sessionService.hasLoggedUser());
    }

    @Test
    void shouldThrowExceptionWhenLogInWhileUserIsAlreadyLoggedIn() {
        // given
        logOutIfSessionIsActive();
        sessionService = SessionService.getInstance();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");
        sessionService.logIn(userAccount);

        // when
        Exception actualExceptionThrown = assertThrows(LogInWhileActiveSessionIsPresentException.class, () -> sessionService.logIn(userAccount));

        // then
        String expectedExceptionMessage = "Can not log in while there is active session.";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldThrowExceptionWhenLogOutWhileNoUserIsLoggedIn() {
        // given
        logOutIfSessionIsActive();
        sessionService = SessionService.getInstance();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");

        // when
        Exception actualExceptionThrown = assertThrows(LogOutWithoutLoggedInUserException.class, () -> sessionService.signOut());

        // then
        String expectedExceptionMessage = "Unable to log out without active log in session.";
        String actualExceptionMessage = actualExceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }
    
    @Test
    void shouldReturnCorrectUserWhenLogIn() {
        // given
        logOutIfSessionIsActive();
        sessionService = SessionService.getInstance();
        userAccount = UserAccountFactory.createUserAccount("Username", "Password");
        sessionService.logIn(userAccount);

        //when
        UserAccount actualUserAccount = sessionService.getLoggedUser();

        //then
        UserAccount expectedUserAccount = userAccount;
        assertEquals(expectedUserAccount, actualUserAccount);
    }

    private void logOutIfSessionIsActive() {
        if (SessionService.getInstance().hasLoggedUser()) {
            SessionService.getInstance().signOut();
        }
    }
}