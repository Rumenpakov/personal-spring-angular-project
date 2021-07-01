package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.AccountCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCredentialsRepository extends JpaRepository<AccountCredentials, Long> {

}
