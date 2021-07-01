package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.AccessDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessDetailsRepository extends JpaRepository<AccessDetails, Long> {

}
