package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.UserAccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountHistoryRepository extends JpaRepository<UserAccountHistory, Long> {

}
