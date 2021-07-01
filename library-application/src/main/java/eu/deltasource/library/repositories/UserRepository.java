package eu.deltasource.library.repositories;

import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.exceptions.AccountNotFoundException;
import eu.deltasource.library.exceptions.UserNameNotAvailableException;
import eu.deltasource.library.repositories.jpaRepositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Contains all registered users, and the methods required to search for users and add
 * users to the repository.
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountDetailsRepository userAccountDetailsRepository;
    private final UserAccountHistoryRepository userAccountHistoryRepository;
    private final AccountCredentialsRepository accountCredentialsRepository;
    private final LocationRepository locationRepository;
    private final NameRepository nameRepository;
//    private final Set<UserAccount> userAccounts = new HashSet<>();


    /**
     * Adds new user account to the repository.
     *
     * @param userAccount User account to add
     */
    public void addNewUserAccount(UserAccount userAccount) {
        if (isUserNameTaken(userAccount.getUserName())) {
            throw new UserNameNotAvailableException("Username is already taken. Value passed: " + userAccount.getUserName());
        }
//        encodeUserCredentials(userAccount);
        accountCredentialsRepository.save(userAccount.getUserAccountDetails().getAccountCredentials());
        locationRepository.save(userAccount.getUserAccountDetails().getLocation());
        nameRepository.save(userAccount.getUserAccountDetails().getName());
        userAccountDetailsRepository.save(userAccount.getUserAccountDetails());
        userAccountHistoryRepository.save(userAccount.getUserAccountHistory());
        userAccountRepository.save(userAccount);
    }

//    private void encodeUserCredentials(UserAccount userAccount) {
//        userAccount.getUserAccountDetails().getAccountCredentials()
//                .encodePassword(passwordEncoder.encode(userAccount.getUserAccountDetails().
//                        getAccountCredentials().getPassword()));
//    }

    /**
     * Checks if given username is taken.
     *
     * @param userName Username to check
     * @return True if taken
     */
    private boolean isUserNameTaken(String userName) {
        return findAccountByUsername(userName).isPresent();
    }

    /**
     * Searches for account by given username.
     *
     * @param userName Username to search by
     * @return User account found
     */
    public Optional<UserAccount> findAccountByUsername(String userName) {
        return userAccountRepository.findUserAccountByUserAccountDetails_AccountCredentials_Username(userName);
    }

    /**
     * Removes user account from the repository.
     *
     * @param userName Username of the account to remove
     */
    public void removeUserAccount(String userName) {
        Optional<UserAccount> userAccountToRemove = findAccountByUsername(userName);
        if (userAccountToRemove.isPresent()) {
            userAccountRepository.delete(userAccountToRemove.get());
        } else {
            throw new AccountNotFoundException("Account to remove not found");
        }
    }

    public long getUsersCount() {
        return userAccountRepository.count();
    }

    /**
     * Removes all existing users.
     */
    public void clear() {
        userAccountRepository.deleteAll();
    }

    /**
     * Retrieves all registered users.
     *
     * @return All registered users
     */
    public Iterable<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }
}
