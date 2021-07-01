package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.UserAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountDetailsRepository extends JpaRepository<UserAccountDetails, Long> {

}
