// src/model/categories/CategoryFactory.java
package model.categories;

public class CategoryFactory {
    public static Category createCategory(String type) {
        return switch (type) {
            case "Transport" -> new Transport();
            case "Food" -> new Food();
            case "Personal Spending" -> new PersonalSpending();
            default -> throw new IllegalArgumentException("Unknown category type: " + type);
        };
    }
}
