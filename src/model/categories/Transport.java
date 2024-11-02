package model.categories;

public class Transport implements Category {
    @Override
    public void addExpense(double amount, String detail) {
        System.out.println("Transport Expense Added: " + amount + " - " + detail);
    }

    @Override
    public void viewExpenses() {
        System.out.println("Viewing all Transport expenses.");
    }
}
