package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.EBookInfo;
import eu.deltasource.library.entities.PaperBookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EBookRepository extends JpaRepository<EBookInfo, Long> {

    Optional<EBookInfo> findByBook_Isbn(String isbn);
}
