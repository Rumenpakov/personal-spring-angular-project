package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

}
