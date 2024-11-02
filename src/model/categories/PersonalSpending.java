package model.categories;

public class PersonalSpending implements Category {
    @Override
    public void addExpense(double amount, String detail) {
        System.out.println("Personal Spending Expense Added: " + amount + " - " + detail);
    }

    @Override
    public void viewExpenses() {
        System.out.println("Viewing all Personal Spending expenses.");
    }
}
