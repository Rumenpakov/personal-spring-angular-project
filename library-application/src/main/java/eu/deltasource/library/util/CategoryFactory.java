package eu.deltasource.library.util;

import eu.deltasource.library.entities.enums.Categories;

public class CategoryFactory {

    public static Categories stringToCategory(String category) {
        switch (category) {
            case "Love":
                return Categories.LOVE;
            case "Romantic":
                return Categories.ROMANTIC;
            case "Gross":
                return Categories.GROSS;
        }
        return null;
    }
}
