package utils;

import model.categories.Category;

public abstract class ExpenseDecorator implements Category {
    protected Category decoratedCategory;

    public ExpenseDecorator(Category decoratedCategory) {
        this.decoratedCategory = decoratedCategory;
    }

    @Override
    public void addExpense(double amount, String detail) {
        decoratedCategory.addExpense(amount, detail);
    }

    @Override
    public void viewExpenses() {
        decoratedCategory.viewExpenses();
    }
}
