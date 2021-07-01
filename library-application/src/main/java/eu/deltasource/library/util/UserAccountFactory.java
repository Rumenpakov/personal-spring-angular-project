package eu.deltasource.library.util;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.enums.Gender;

/**
 * This class is used to create user account based on user name and password. The created user account is for
 * dummy purposes.
 */
public class UserAccountFactory {

    /**
     * Creates dummy user account based on given user name and password.
     *
     * @param userName given user name
     * @param password given password
     * @return created user account
     */
    public static UserAccount createUserAccount(String userName, String password) {
        Name name = new Name("Rumen Pakov");
        Location location = new Location("Random street 1", "Plovdiv", "Bulgaria");
        AccountCredentials accountCredentials = new AccountCredentials(userName, password);
        UserAccountDetails userAccountDetails = UserAccountDetails.builder().name(name)
                .location(location)
                .age(10)
                .accountCredentials(accountCredentials)
                .email("testmail@abv.bg")
                .gender(Gender.MALE)
                .build();
        return new UserAccount(userAccountDetails);
    }
}
