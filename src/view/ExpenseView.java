package view;

import controller.ExpenseController;

import java.util.Scanner;

public class ExpenseView {
    private ExpenseController controller;
    private Scanner scanner;

    public ExpenseView(ExpenseController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> controller.viewExpenses();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addExpense() {
        scanner.nextLine(); // consume newline
        System.out.print("Enter category (Transport/Food/Personal Spending): ");
        String category = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        System.out.print("Enter detail: ");
        String detail = scanner.nextLine();

        controller.addExpense(category, amount, detail);
        System.out.println("Expense added successfully.");
    }
}
