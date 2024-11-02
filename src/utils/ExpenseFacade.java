package utils;

import model.SingletonTracker;
import model.CategoryFactory;
import model.categories.Category;

public class ExpenseFacade {
    private SingletonTracker tracker;

    public ExpenseFacade() {
        tracker = SingletonTracker.getInstance();
    }

    public void addExpense(String categoryType, double amount, String detail) {
        Category category = CategoryFactory.createCategory(categoryType);
        category.addExpense(amount, detail);
        tracker.addExpense(categoryType, amount, detail);
    }

    public void viewExpenses() {
        tracker.viewExpenses();
    }
}
