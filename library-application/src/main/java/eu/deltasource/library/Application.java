package eu.deltasource.library;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.deltasource.library.repositories.jpaRepositories.AuthorRepostiry;
import eu.deltasource.library.repositories.jpaRepositories.BookJpaRepository;
import eu.deltasource.library.repositories.jpaRepositories.PaperBookRepository;
import eu.deltasource.library.repositories.jpaRepositories.NameRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

/**
 * This class is where the application gets assembled together.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Library api", version = "0.1", description = "Api for interactions with library"))
@Slf4j
@EnableTransactionManagement
public class Application {

    @Autowired
    private LibraryDemoExecutor libraryDemoExecutor;

    @PostConstruct
    public void run() throws Exception {
        libraryDemoExecutor.exec();
    }

    @Bean
    CommandLineRunner commandLineRunner(AuthorRepostiry authorRepostiry,
                                        NameRepository nameRepository,
                                        BookJpaRepository bookJpaRepository,
                                        PaperBookRepository paperBookRepository) {
        return args -> {
//            Set<Author> authors = new HashSet<>();
//            Name name = new Name("AuthorFirstName", "AuthorLastName");
//            Author author = new Author(name, "Bulgaria", LocalDate.now());
//            authors.add(author);
//            Book book = new Book("Title", authors, "ShortSummary", "ImgUrl", "isbn123");
//            PaperBookInfo entity = new PaperBookInfo(1, book);
//
//            nameJpaRepository.save(name);
//            authorJpaRepostiry.save(author);
//            bookJpaRepository.save(book);
//            paperBookJpaRepository.save(entity);
//
//            Set<Author> authors1 = new HashSet<>();
//            Name name1 = new Name("AuthorFirstName", "AuthorLastName");
//            Author author1 = new Author(name1, "Bulgaria", LocalDate.now());
//            authors.add(author1);
//            Book book1 = new Book("Title", authors1, "ShortSummary", "ImgUrl", "isbn123");
//            PaperBookInfo entity1 = new PaperBookInfo(1, book1);
//
//            nameJpaRepository.save(name1);
//            authorJpaRepostiry.save(author1);
//            bookJpaRepository.save(book1);
//            paperBookJpaRepository.save(entity1);
//
//            Set<Author> authors2 = new HashSet<>();
//            Name name2 = new Name("AuthorFirstName", "AuthorLastName");
//            Author author2 = new Author(name2, "Bulgaria", LocalDate.now());
//            authors.add(author2);
//            Book book2 = new Book("Title", authors2, "ShortSummary", "ImgUrl", "isbn223");
//            PaperBookInfo entity2 = new PaperBookInfo(2, book2);
//
//            nameJpaRepository.save(name2);
//            authorJpaRepostiry.save(author2);
//            bookJpaRepository.save(book2);
//            paperBookJpaRepository.save(entity2);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}