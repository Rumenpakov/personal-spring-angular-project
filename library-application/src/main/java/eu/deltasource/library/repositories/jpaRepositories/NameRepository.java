package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

}
