package model;

import model.categories.*;

public class CategoryFactory {
    public static Category createCategory(String type) {
        switch (type.toLowerCase()) {
            case "transport":
                return new Transport();
            case "food":
                return new Food();
            case "personal spending":
                return new PersonalSpending();
            default:
                throw new IllegalArgumentException("Unknown category type");
        }
    }
}
