package eu.deltasource.library.services;

import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.models.LogInModel;
import eu.deltasource.library.entities.models.SignUpModel;
import eu.deltasource.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public void signUp(SignUpModel signUpModel) {
        UserAccountDetails userAccountDetails = UserAccountDetails.builder()
                .accountCredentials(new AccountCredentials(signUpModel.getUsername(), signUpModel.getPassword()))
                .name(new Name(signUpModel.getName()))
                .age(signUpModel.getAge())
                .email(signUpModel.getEmail())
                .gender(signUpModel.getGender())
                .location(new Location(
                        signUpModel.getAddress(),
                        signUpModel.getCity(),
                        signUpModel.getCountry()
                )).build();
        UserAccount userAccount = new UserAccount(userAccountDetails);
        userRepository.addNewUserAccount(userAccount);
    }
}
