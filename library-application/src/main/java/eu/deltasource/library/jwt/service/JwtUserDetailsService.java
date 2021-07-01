package eu.deltasource.library.jwt.service;

import eu.deltasource.library.entities.AccountCredentials;
import eu.deltasource.library.entities.UserAccount;
import eu.deltasource.library.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username != null && !username.isEmpty()) {
            Optional<UserAccount> foundAccount = userRepository.findAccountByUsername(username);
            if (foundAccount.isPresent()) {
                AccountCredentials userAccountCredentials = foundAccount.get().getUserAccountDetails().getAccountCredentials();
                return new User(userAccountCredentials.getUsername(), userAccountCredentials.getPassword(), new ArrayList<>());
            } else {
                throw new UsernameNotFoundException("User with username " + username + " not found");
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
