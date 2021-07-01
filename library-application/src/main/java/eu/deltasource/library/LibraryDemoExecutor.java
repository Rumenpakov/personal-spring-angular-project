package eu.deltasource.library;

import eu.deltasource.library.controllers.BookController;
import eu.deltasource.library.controllers.LoginController;
import eu.deltasource.library.controllers.UserBooksController;
import eu.deltasource.library.controllers.UserController;
import eu.deltasource.library.entities.*;
import eu.deltasource.library.entities.enums.Categories;
import eu.deltasource.library.entities.enums.Gender;
import eu.deltasource.library.entities.models.LogInModel;
import eu.deltasource.library.repositories.jpaRepositories.*;
import eu.deltasource.library.services.AuthService;
import eu.deltasource.library.util.BookFactory;
import eu.deltasource.library.util.UserAccountFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryDemoExecutor {

    
    private final BookController bookController;
    private final UserController userController;
    private final LoginController loginController;
    private final UserBooksController userBooksController;
    private final PaperBookRepository paperBookRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountDetailsRepository userAccountDetailsRepository;
    private final UserAccountHistoryRepository userAccountHistoryRepository;
    private final AccountCredentialsRepository accountCredentialsRepository;
    private final LocationRepository locationRepository;
    private final NameRepository nameRepository;
    private final AuthService authService;

    public void exec() {
        //init objects
        PaperBookInfo paperBookInfoToAddToRepo = BookFactory.createPaperBookInfo(1);
        EBookInfo eBookInfoToAddToRepo = BookFactory.createEBookInfo();
        UserAccount userAccountToAddToRepo = UserAccountFactory.createUserAccount("Username", "Password");
        UserAccount userAccountToAddToRepo2 = UserAccountFactory.createUserAccount("Username2", "Password");
        bookController.addNewEBook(eBookInfoToAddToRepo);
//        bookController.addNewPaperBook(paperBookInfoToAddToRepo);
        userController.addNewUserAccount(userAccountToAddToRepo);
        userController.addNewUserAccount(userAccountToAddToRepo2);

        //demo cases
        loginController.logIn("Username", "Password");
        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));

//        loginController.logOut();
//        loginController.logIn("Username2", "Password");
//
//        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));
//
//        loginController.logOut();
//        loginController.logIn("Username", "Password");
//
//        userBooksController.returnBookByIsbn("123isbn");
//
//        loginController.logOut();
//        loginController.logIn("Username2", "Password");
//
//        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));
//        System.out.println(userBooksController.findBorrowedBookByIsbn("123isbn"));
//
//        loginController.logOut();
//        loginController.logIn("Username", "Password");
//
//        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));
//        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));
//        System.out.println(userBooksController.tryToBorrowBook(paperBookInfoToAddToRepo));

        Set<Categories> loveGross = new HashSet<>();
        loveGross.add(Categories.LOVE);
        loveGross.add(Categories.GROSS);

        Set<Categories> romanticGross = new HashSet<>();
        romanticGross.add(Categories.ROMANTIC);
        romanticGross.add(Categories.GROSS);


        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("1", "Mussolini: The Rise and Fall of Il Duce",
                "With his signature insight and compelling style, Christopher Hibbert explains the extraordinary complexities and contradictions that characterized Benito Mussolini. Mussolini was born on a Sunday afternoon in 1883 in a village in central Italy. On a Saturday afternoon in 1945 he was shot by",
                "https://images-na.ssl-images-amazon.com/images/I/41Ehij-VjaL._SX322_BO1,204,203,200_.jpg", "Taner Kirilov", Categories.LOVE, Categories.ROMANTIC, Categories.GROSS));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("2", "Adulting: How to Become a Grown-up in 468 Easy(ish) Steps",
                "If you graduated from college but still feel like a student . . . if you wear a business suit to job interviews but pajamas to the grocery store . . . if you have your own apartment but no idea how to cook or clean . . . it's OK. But it doesn't have to be this way.",
                "https://images-na.ssl-images-amazon.com/images/I/510VK8Q1dbL._SX328_BO1,204,203,200_.jpg", "Rumen Rumenov", Categories.ROMANTIC, Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("3", "Ariadne: A Novel",
                "Ariadne, Princess of Crete, grows up greeting the dawn from her beautiful dancing floor and listening to her nursemaid’s stories of gods and heroes. But beneath her golden palace echo the ever-present hoofbeats of her brother, the Minotaur, a monster who demands blood sacrifice.",
                "https://images-na.ssl-images-amazon.com/images/I/41AvaBmddFL._SX329_BO1,204,203,200_.jpg", "Ivan Ivanov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("4", "Memoirs of the Second World War",
                "As Prime Minister of Great Britain from 1940 to 1945, Winston Churchill was not only the most powerful player in World War II but also the free world's most eloquent voice of defiance in the face of Nazi tyranny. Churchill's epic accounts of those times",
                "https://images-na.ssl-images-amazon.com/images/I/41yHk8L0EZL._SX331_BO1,204,203,200_.jpg", "Taner Tanerov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("8", "The Premonition: A Pandemic Story",
                "Fortunately, we are still a nation of skeptics. Fortunately, there are those among us who study pandemics and are willing to look unflinchingly at worst-case scenarios. Michael Lewis’s taut and brilliant nonfiction thriller pits a band of medical visionaries against the wall of ignorance that was the official response of the Trump administration to the outbreak of COVID-19.",
                "https://images-na.ssl-images-amazon.com/images/I/41+2DiWeWAS._SX345_BO1,204,203,200_.jpg", "Petar Ivanov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("5", "Adulting: How to Become a Grown-up in 468 Easy(ish) Steps",
                "If you graduated from college but still feel like a student . . . if you wear a business suit to job interviews but pajamas to the grocery store . . . if you have your own apartment but no idea how to cook or clean . . . it's OK. But it doesn't have to be this way.",
                "https://images-na.ssl-images-amazon.com/images/I/41+2DiWeWAS._SX345_BO1,204,203,200_.jpg", "Rumen Ivanov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("6", "Ariadne: A Novel",
                "Ariadne, Princess of Crete, grows up greeting the dawn from her beautiful dancing floor and listening to her nursemaid’s stories of gods and heroes. But beneath her golden palace echo the ever-present hoofbeats of her brother, the Minotaur, a monster who demands blood sacrifice.",
                "https://images-na.ssl-images-amazon.com/images/I/41AvaBmddFL._SX329_BO1,204,203,200_.jpg", "Petar Petrov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("7", "Mussolini: The Rise and Fall of Il Duce",
                "With his signature insight and compelling style, Christopher Hibbert explains the extraordinary complexities and contradictions that characterized Benito Mussolini. Mussolini was born on a Sunday afternoon in 1883 in a village in central Italy. On a Saturday afternoon in 1945 he was shot by",
                "https://images-na.ssl-images-amazon.com/images/I/41Ehij-VjaL._SX322_BO1,204,203,200_.jpg", "Taner Tanerov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("16", "The Premonition: A Pandemic Story",
                "Fortunately, we are still a nation of skeptics. Fortunately, there are those among us who study pandemics and are willing to look unflinchingly at worst-case scenarios. Michael Lewis’s taut and brilliant nonfiction thriller pits a band of medical visionaries against the wall of ignorance that was the official response of the Trump administration to the outbreak of COVID-19.",
                "https://images-na.ssl-images-amazon.com/images/I/41+2DiWeWAS._SX345_BO1,204,203,200_.jpg", "Ivan Georgiev", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("9", "Adulting: How to Become a Grown-up in 468 Easy(ish) Steps",
                "If you graduated from college but still feel like a student . . . if you wear a business suit to job interviews but pajamas to the grocery store . . . if you have your own apartment but no idea how to cook or clean . . . it's OK. But it doesn't have to be this way.",
                "https://images-na.ssl-images-amazon.com/images/I/510VK8Q1dbL._SX328_BO1,204,203,200_.jpg", "Rumen Petrov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("10", "Ariadne: A Novel",
                "Ariadne, Princess of Crete, grows up greeting the dawn from her beautiful dancing floor and listening to her nursemaid’s stories of gods and heroes. But beneath her golden palace echo the ever-present hoofbeats of her brother, the Minotaur, a monster who demands blood sacrifice.",
                "https://images-na.ssl-images-amazon.com/images/I/41AvaBmddFL._SX329_BO1,204,203,200_.jpg", "Georgi Traqnov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("11", "Impostor Syndrome: A Novel",
                "A sharp and prescient novel about women in the workplace, the power of Big Tech, and the looming threat of foreign espionage from Kathy Wang, “a skilled satirist of the northern California dream” (Harper’s Bazaar)",
                "https://images-na.ssl-images-amazon.com/images/I/41hwpqxplYL._SX331_BO1,204,203,200_.jpg", "Rumen Krasimirov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("12", "The Premonition: A Pandemic Story",
                "Fortunately, we are still a nation of skeptics. Fortunately, there are those among us who study pandemics and are willing to look unflinchingly at worst-case scenarios. Michael Lewis’s taut and brilliant nonfiction thriller pits a band of medical visionaries against the wall of ignorance that was the official response of the Trump administration to the outbreak of COVID-19.",
                "https://images-na.ssl-images-amazon.com/images/I/41+2DiWeWAS._SX345_BO1,204,203,200_.jpg", "Georgi Rumenov", Categories.GROSS));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("13", "Mussolini: The Rise and Fall of Il Duce",
                "With his signature insight and compelling style, Christopher Hibbert explains the extraordinary complexities and contradictions that characterized Benito Mussolini. Mussolini was born on a Sunday afternoon in 1883 in a village in central Italy. On a Saturday afternoon in 1945 he was shot by",
                "https://images-na.ssl-images-amazon.com/images/I/41Ehij-VjaL._SX322_BO1,204,203,200_.jpg", "Taner Tanerov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("14", "Ariadne: A Novel",
                "Ariadne, Princess of Crete, grows up greeting the dawn from her beautiful dancing floor and listening to her nursemaid’s stories of gods and heroes. But beneath her golden palace echo the ever-present hoofbeats of her brother, the Minotaur, a monster who demands blood sacrifice.",
                "https://images-na.ssl-images-amazon.com/images/I/41AvaBmddFL._SX329_BO1,204,203,200_.jpg", "Georgi Petrov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("15", "Impostor Syndrome: A Novel",
                "A sharp and prescient novel about women in the workplace, the power of Big Tech, and the looming threat of foreign espionage from Kathy Wang, “a skilled satirist of the northern California dream” (Harper’s Bazaar)",
                "https://images-na.ssl-images-amazon.com/images/I/41hwpqxplYL._SX331_BO1,204,203,200_.jpg", "Rumen Georgiev", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("17", "Gracious: A Practical Primer on Charm, Tact, and Unsinkable Strength",
                "Graciousness is practicing the arts of kindness, thoughtfulness, good manners, humanity, and, well, basic decency. It’s not about memorizing every rule of traditional etiquette (though there is something to",
                "https://images-na.ssl-images-amazon.com/images/I/51nkEGLvAKL._SX324_BO1,204,203,200_.jpg", "Ivan Ivanov", Categories.ROMANTIC));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("18", "Memoirs of the Second World War",
                "As Prime Minister of Great Britain from 1940 to 1945, Winston Churchill was not only the most powerful player in World War II but also the free world's most eloquent voice of defiance in the face of Nazi tyranny. Churchill's epic accounts of those times",
                "https://images-na.ssl-images-amazon.com/images/I/41yHk8L0EZL._SX331_BO1,204,203,200_.jpg", "Taner Ivanov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("19", "Protocol: The Power of Diplomacy and How to Make It Work for You",
                "History often appears to consist of big gestures and dramatic shifts. But for every peace treaty signed, someone set the stage, using hidden influence to effect the outcome. In her roles as chief of protocol for President Barack Obama and social secretary to President Bill Clinton and First Lady Hillary Clinton,",
                "https://images-na.ssl-images-amazon.com/images/I/41qPGxE8fqL._SY291_BO1,204,203,200_QL40_FMwebp_.jpg", "Vasil", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("20", "A Promised Land",
                "A riveting, deeply personal account of history in the making—from the president who inspired us to believe in the power of democracy",
                "https://images-na.ssl-images-amazon.com/images/I/41L5qgUW2fL._SX327_BO1,204,203,200_.jpg", "Taner Ivanov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("21", "Rage",
                "Bob Woodward’s new book, Rage, is an unprecedented and intimate tour de force of new reporting on the Trump presidency facing a global pandemic, economic disaster and racial unrest.",
                "https://images-na.ssl-images-amazon.com/images/I/41oPBg75BfL._SX329_BO1,204,203,200_.jpg", "Taner Ivanov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("22", "Decision Points",
                "George W. Bush served as president of the United States during eight of the most consequential years in American history. The decisions that reached his desk impacted people around the world and defined the times in which we live.",
                "https://images-na.ssl-images-amazon.com/images/I/41louJzbbmL._SX327_BO1,204,203,200_.jpg", "Taner Ivanov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("23", "Reagan: The Life",
                "In his magisterial new biography, H. W. Brands brilliantly establishes Ronald Reagan as one of the two great presidents of the twentieth century, a true peer to Franklin Roosevelt. Reagan conveys with sweep",
                "https://images-na.ssl-images-amazon.com/images/I/41o573qw2AL._SX327_BO1,204,203,200_.jpg", "Aleksandar Petrov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("24", "Lincoln",
                "Donald brilliantly depicts Lincoln’s gradual ascent from humble beginnings in rural Kentucky to the ever-expanding political circles in Illinois, and finally to the presidency of a country divided by civil war. Donald goes beyond biography, illuminating the gradual development of Lincoln’s character, chronicling his",
                "https://images-na.ssl-images-amazon.com/images/I/51EhZ9DRQVL._SY291_BO1,204,203,200_QL40_FMwebp_.jpg", "Vasil Noten", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("25", "Eisenhower in War and Peace",
                "In this extraordinary volume, Jean Edward Smith presents a portrait of Dwight D. Eisenhower that is as full, rich, and revealing as anything ever written about America’s thirty-fourth president. Here is",
                "https://images-na.ssl-images-amazon.com/images/I/5117RSGMD7L._SX329_BO1,204,203,200_.jpg", "Aleksandar Ivanov", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("26", "Memoirs of the Second World War",
                "As Prime Minister of Great Britain from 1940 to 1945, Winston Churchill was not only the most powerful player in World War II but also the free world's most eloquent voice of defiance in the face of Nazi tyranny. Churchill's epic accounts of those times",
                "https://images-na.ssl-images-amazon.com/images/I/41yHk8L0EZL._SX331_BO1,204,203,200_.jpg", "Georgi Terziev", Categories.LOVE));
        bookController.addNewPaperBookJpa(BookFactory.createPaperBookInfo("27", "Mussolini: The Rise and Fall of Il Duce",
                "With his signature insight and compelling style, Christopher Hibbert explains the extraordinary complexities and contradictions that characterized Benito Mussolini. Mussolini was born on a Sunday afternoon in 1883 in a village in central Italy. On a Saturday afternoon in 1945 he was shot by",
                "https://images-na.ssl-images-amazon.com/images/I/41Ehij-VjaL._SX322_BO1,204,203,200_.jpg", "Taner Ivanov", Categories.LOVE));

        log.info(paperBookRepository.findAll(PageRequest.of(0, 6)).getTotalPages() + "");
        UserAccountDetails userAccountDetails = UserAccountDetails.builder()
                .accountCredentials(new AccountCredentials("Rumen", "Pakov"))
                .age(20)
                .email("Email")
                .location(new Location("Petko d. petkov", "Plovdiv", "Bulgaria"))
                .name(new Name("Rumen Pakov"))
                .gender(Gender.MALE)
                .build();
        log.info(userAccountDetails.toString());
        UserAccount userAccount = new UserAccount(userAccountDetails);
        accountCredentialsRepository.save(userAccountDetails.getAccountCredentials());
        locationRepository.save(userAccount.getUserAccountDetails().getLocation());
        nameRepository.save(userAccount.getUserAccountDetails().getName());
        userAccountDetailsRepository.save(userAccountDetails);
        userAccountHistoryRepository.save(userAccount.getUserAccountHistory());
        userAccountRepository.save(userAccount);
        log.info(userAccountRepository.findUserAccountByUserAccountDetails_AccountCredentials_Username("Rumen").toString());
    }
}
