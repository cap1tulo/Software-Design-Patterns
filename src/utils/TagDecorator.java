package utils;

import model.categories.Category;

public class TagDecorator extends ExpenseDecorator {
    private String tag;

    public TagDecorator(Category decoratedCategory, String tag) {
        super(decoratedCategory);
        this.tag = tag;
    }

    @Override
    public void addExpense(double amount, String detail) {
        super.addExpense(amount, detail);
        System.out.println("Tagged with: " + tag);
    }
}
