// src/view/ExpenseViewObserver.java
package view;

import observer.ExpenseObserver;

public class ExpenseViewObserver implements ExpenseObserver {
    @Override
    public void update(String category, String detail, double amount) {
        System.out.println("Observer Notification: " + detail + " in " + category + " updated by $" + amount);
    }
}
