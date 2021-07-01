package eu.deltasource.library.util;

import eu.deltasource.library.entities.PaperBookInfo;
import eu.deltasource.library.entities.enums.Categories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class SpecificationFactory {

    public static Specification<PaperBookInfo> hasAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return (paperBookInfoRoot, cq, cb) -> cb.conjunction();
        }
        return (paperBookInfoRoot, cq, cb) -> cb.like(paperBookInfoRoot.join("book").join("authors").get("name").get("name"), "%" + authorName + "%");
    }

    public static Specification<PaperBookInfo> titleContains(String title) {
        if (title == null || title.trim().isEmpty()) {
            return (paperBookInfoRoot, cq, cb) -> cb.conjunction();
        }
        return (paperBookInfoRoot, cq, cb) -> cb.like(paperBookInfoRoot.get("book").get("title"), "%" + title + "%");
    }

    public static Specification<PaperBookInfo> hasCategories(String categories) {
        if (categories == null || categories.trim().isEmpty()) {
            return (paperBookInfoRoot, cq, cb) -> cb.conjunction();
        }
        List<String> categoriesAsString = Arrays.asList(categories.split(","));
        Set<Categories> categoriesAfterConversion = new HashSet<>();
        categoriesAsString.forEach(item -> categoriesAfterConversion.add(CategoryFactory.stringToCategory(item)));
        log.info(categoriesAfterConversion.toString());
        return (paperBookInfoRoot, cq, cb) -> paperBookInfoRoot.join("book").join("categories").in(categoriesAfterConversion);
    }
}
