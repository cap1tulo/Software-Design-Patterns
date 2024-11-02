package model;

public class SingletonTracker {
    private static SingletonTracker instance;

    private SingletonTracker() {
        // Private constructor to prevent instantiation
    }

    public static SingletonTracker getInstance() {
        if (instance == null) {
            instance = new SingletonTracker();
        }
        return instance;
    }

    public void addExpense(String category, double amount, String detail) {
        // Method to add an expense
        System.out.println("Adding expense: " + category + " - " + amount + " - " + detail);
    }

    public void viewExpenses() {
        // Placeholder method to view expenses
        System.out.println("Viewing all expenses.");
    }
}
