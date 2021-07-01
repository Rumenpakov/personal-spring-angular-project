package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepostiry extends JpaRepository<Author, Long> {

}
