package eu.deltasource.library.repositories;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.exceptions.UserNameNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eu.deltasource.library.util.UserAccountFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void initializeVariables() {
//        userRepository = new UserRepository();
    }

    @Test
    void shouldSuccessfullyAddNewUser() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");

        //when
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //then
        Optional<UserAccount> actualUserAccountFound = userRepository.findAccountByUsername("Username");
        assertTrue(actualUserAccountFound.isPresent());
    }

    @Test
    void shouldSuccessfullyRemoveExistingUser() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        String userNameOfExistingAccount = userAccountToBeAddedToRepository.getUserName();
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        userRepository.removeUserAccount(userNameOfExistingAccount);

        //then
        long expectedUsersCount = 0;
        long actualUsersCount = userRepository.getUsersCount();
        assertEquals(expectedUsersCount, actualUsersCount);
    }

    @Test
    void shouldSuccessfullyFindExistingUser() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        Optional<UserAccount> actualUserFound = userRepository.findAccountByUsername("Username");

        //then
        assertTrue(actualUserFound.isPresent());
    }

    @Test
    void shouldThrowCorrectExceptionOnSearchingForUnexistingUser() {
        //given

        //when
        Optional<UserAccount> foundUserAccount = userRepository.findAccountByUsername("Username");

        //then
        assertEquals(Optional.empty(), foundUserAccount);
    }

    @Test
    void shouldReturnCorrectCountOfUsers() {
        //given
        UserAccount userAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Username", "Password");
        userRepository.addNewUserAccount(userAccountToBeAddedToRepository);

        //when
        long actualCountOfUsers = userRepository.getUsersCount();

        //then
        long expectedCountOfUsers = 1;
        assertEquals(expectedCountOfUsers, actualCountOfUsers);
    }

    @Test
    void shouldNotAddNewAccountIfUsernameIsTaken() {
        //given
        UserAccount firstUserAccountToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen", "Password");
        UserAccount secondUserAccountWithSameNameToBeAddedToRepository = UserAccountFactory.createUserAccount("Rumen", "Password");
        userRepository.addNewUserAccount(firstUserAccountToBeAddedToRepository);

        //when
        Exception exceptionThrown = assertThrows(UserNameNotAvailableException.class,
                () -> userRepository.addNewUserAccount(secondUserAccountWithSameNameToBeAddedToRepository));

        //then
        String expectedExceptionMessage = "Username is already taken.";
        String actualExceptionMessage = exceptionThrown.getMessage();
        assertTrue(actualExceptionMessage.contains(expectedExceptionMessage));
    }
}