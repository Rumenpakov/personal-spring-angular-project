package eu.deltasource.library.controllers;

import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.exceptions.IllegalInputException;
import eu.deltasource.library.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This class is responsible for adding new users, removing and finding already registered users.
 * Method for the latter interactions are presented.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds new user account to the repository.
     *
     * @param userAccount User account to add
     */
    public void addNewUserAccount(UserAccount userAccount) {
        if (userAccount == null) {
            throw new IllegalInputException("User account to be added in repository can not be null");
        }
        userRepository.addNewUserAccount(userAccount);
    }

    /**
     * Removes user account from the repository.
     *
     * @param username Username of the account to remove
     */
    public void deleteUserAccount(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalInputException("Username to delete account by can not be null");
        }
        userRepository.removeUserAccount(username);
    }

    /**
     * Searches for user account by username.
     *
     * @param userName Username to search by
     * @return Found user account
     */
    public Optional<UserAccount> findUserAccountByUsername(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalInputException("Username to search account by can not be null or empty");
        }
        return userRepository.findAccountByUsername(userName);
    }

    /**
     * Retrieves count of users in repository.
     *
     * @return Count of users in repository
     */
    public long getUsersCount() {
        return userRepository.getUsersCount();
    }

    @GetMapping
    @Operation(summary = "Get all User Accounts", description = "Fetch all User Accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<Iterable<UserAccount>> getAllUserAccounts() {
        return ResponseEntity.ok(userRepository.getAll());
    }

    @GetMapping(path = "/{userName}")
    @Operation(summary = "Get User Account", description = "Fetch User Account by given username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content())
    })
    public ResponseEntity<UserAccount> getUserAccountByUserName(@PathVariable String userName) {
        Optional<UserAccount> foundUserAccount = findUserAccountByUsername(userName);
        return foundUserAccount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(path = "/{userName}")
    @Operation(summary = "Get User Account", description = "Fetch User Account by given username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")

    })
    public void deleteUserAccountByUserName(@PathVariable String userName) {
        deleteUserAccount(userName);
    }
}
