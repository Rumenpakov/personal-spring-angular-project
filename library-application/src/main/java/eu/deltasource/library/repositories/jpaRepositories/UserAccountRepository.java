package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findUserAccountByUserAccountDetails_AccountCredentials_Username(String userName);
}
