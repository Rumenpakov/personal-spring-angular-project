package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.repositories.UserRepository;
import eu.deltasource.library.util.UserAccountFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerTest {

    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    public void initializeVariables() {
//        userRepository = new UserRepository();
        userController = new UserController(userRepository);
    }

    @Test
    void shouldSuccessfullyAddNewUserAccountToRepository() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");

        //when
        userController.addNewUserAccount(userAccountToBeAddedToRepository);

        //then
        Optional<UserAccount> actualUserAccountFound = userController.findUserAccountByUsername("Username");
        assertTrue(actualUserAccountFound.isPresent());
    }

    @Test
    void shouldSuccessfullyRemoveExistingUser() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        String userNameOfExistingAccount = userAccountToBeAddedToRepository.getUserName();
        userController.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        userController.deleteUserAccount(userNameOfExistingAccount);

        //then
        long expectedUsersCount = 0;
        long actualUsersCount = userController.getUsersCount();
        assertEquals(expectedUsersCount, actualUsersCount);
    }

    @Test
    void shouldSuccessfullyFindExistingUser() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        Optional<UserAccount> actualUserFound = userController.findUserAccountByUsername("Username");

        //then
        assertTrue(actualUserFound.isPresent());
    }

    @Test
    void shouldReturnCorrectCountOfUsers() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userController.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        long actualCountOfUsers = userController.getUsersCount();

        //then
        long expectedCountOfUsers = 1;
        assertEquals(expectedCountOfUsers, actualCountOfUsers);
    }
}