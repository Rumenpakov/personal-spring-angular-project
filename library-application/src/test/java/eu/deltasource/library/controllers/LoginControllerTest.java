package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.exceptions.IncorrectPasswordException;
import eu.deltasource.library.exceptions.LogInWhileActiveSessionIsPresentException;
import eu.deltasource.library.exceptions.LogOutWithoutLoggedInUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.services.SessionService;
import eu.deltasource.library.util.UserAccountFactory;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {
    private UserRepository userRepository;
    private LoginController loginController;

    @BeforeEach
    public void initializeVariables() {
//        userRepository = new UserRepository();
        loginController = new LoginController(userRepository);
    }

    @Test
    void shouldReturnCorrectAccountOnCorrectCredentials() {
        //given
        logOutIfSessionIsActive();
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        UserAccount actualUserAccount = loginController.logIn("Username", "Password");

        //then
        UserAccount expectedUserAccount = userAccountToBeAddedToRepository;
        assertEquals(expectedUserAccount, actualUserAccount);
    }

    @Test
    void shouldThrowExceptionOnIncorrectPassword() {
        //given
        logOutIfSessionIsActive();
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        Exception exceptionThrown = assertThrows(IncorrectPasswordException.class,
                () -> loginController.logIn("Username", "IncorrectPassword"));

        //then
        String expectedExceptionMessage = "Entered password doesn't match account password.";
        String actualExceptionMessage = exceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void hasLoggedUserShouldBeFalseAfterLogout() {
        //given
        logOutIfSessionIsActive();
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);
        loginController.logIn("Username", "Password");

        //when
        loginController.logOut();

        //then
        assertFalse(loginController.hasLoggedUser());
    }

    @Test
    void shouldThrowExceptionOnLogoutWhenNotLoggedIn() {
        //given
        logOutIfSessionIsActive();

        //when
        Exception exceptionThrown = assertThrows(LogOutWithoutLoggedInUserException.class,
                () -> loginController.logOut());

        //then
        String expectedExceptionMessage = "Unable to log out without active log in session.";
        String actualExceptionMessage = exceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    @Test
    void shouldThrowExceptionOnLogInIfAnotherSessionIsActive() {
        //given
        logOutIfSessionIsActive();
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);
        loginController.logIn("Username", "Password");

        //when
        Exception exceptionThrown = assertThrows(LogInWhileActiveSessionIsPresentException.class,
                () -> loginController.logIn("Username", "Password"));

        //then
        String expectedExceptionMessage = "Can not log in while there is active session.";
        String actualExceptionMessage = exceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }

    private void logOutIfSessionIsActive() {
        if (SessionService.getInstance().hasLoggedUser()) {
            SessionService.getInstance().signOut();
        }
    }
}