package model.categories;

public class Food implements Category {
    @Override
    public void addExpense(double amount, String detail) {
        System.out.println("Food Expense Added: " + amount + " - " + detail);
    }

    @Override
    public void viewExpenses() {
        System.out.println("Viewing all Food expenses.");
    }
}
