// src/observer/ExpenseSubject.java
package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpenseSubject {
    private List<ExpenseObserver> observers = new ArrayList<>();

    public void addObserver(ExpenseObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ExpenseObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(String category, String detail, double amount) {
        for (ExpenseObserver observer : observers) {
            observer.update(category, detail, amount);
        }
    }
}
