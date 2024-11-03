package controller;

import observer.ExpenseSubject;
import utils.ExpenseFacade;

import java.util.HashMap;
import java.util.Map;

public class ExpenseController extends ExpenseSubject {
    private ExpenseFacade facade;
    private Map<String, Double> subWidgetTotals;

    public ExpenseController() {
        this.facade = new ExpenseFacade();
        this.subWidgetTotals = new HashMap<>();

        // Initialize totals for each sub-widget
        subWidgetTotals.put("Taxi", 0.0);
        subWidgetTotals.put("Bus", 0.0);
        subWidgetTotals.put("Scooter", 0.0);
        subWidgetTotals.put("Restaurant", 0.0);
        subWidgetTotals.put("Cafe", 0.0);
        subWidgetTotals.put("Home Products", 0.0);
        subWidgetTotals.put("Orders", 0.0);
        subWidgetTotals.put("Rent Payment", 0.0);
        subWidgetTotals.put("Utility Fee", 0.0);
        subWidgetTotals.put("Gym", 0.0);
        subWidgetTotals.put("Self Care", 0.0);
    }

    public void addExpense(String category, double amount, String detail) {
        facade.addExpense(category, amount, detail);

        // Update the amount for the selected sub-widget
        subWidgetTotals.put(detail, subWidgetTotals.getOrDefault(detail, 0.0) + amount);

        // Notify all observers of the update
        notifyObservers(category, detail, amount);
    }

    public Map<String, Double> getSubWidgetTotals() {
        return subWidgetTotals;
    }

    public void viewExpenses() {
        System.out.println("Current Expenses:");
        for (Map.Entry<String, Double> entry : subWidgetTotals.entrySet()) {
            System.out.println(entry.getKey() + " - $" + entry.getValue());
        }
    }
}
