package command;

import controller.ExpenseController;

public class AddExpenseCommand implements Command {
    private ExpenseController controller;
    private String category;
    private double amount;
    private String detail;

    public AddExpenseCommand(ExpenseController controller, String category, double amount, String detail) {
        this.controller = controller;
        this.category = category;
        this.amount = amount;
        this.detail = detail;
    }

    @Override
    public void execute() {
        controller.addExpense(category, amount, detail);
    }
}
