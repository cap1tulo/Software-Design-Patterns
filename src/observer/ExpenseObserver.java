// src/observer/ExpenseObserver.java
package observer;

public interface ExpenseObserver {
    void update(String category, String detail, double amount);
}
