package eu.deltasource.library.repositories.jpaRepositories;

import eu.deltasource.library.entities.PaperBookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaperBookRepository extends JpaRepository<PaperBookInfo, Long>, JpaSpecificationExecutor<PaperBookInfo> {

    List<PaperBookInfo> findDistinctByBook_TitleContains(String title);
    Optional<PaperBookInfo> findByBook_Isbn(String isbn);
}
