// src/model/SingletonTracker.java
package model;

import java.util.HashMap;
import java.util.Map;

public class SingletonTracker {
    private static SingletonTracker instance;
    private Map<String, Double> totals;

    private SingletonTracker() {
        totals = new HashMap<>();
    }

    public static SingletonTracker getInstance() {
        if (instance == null) {
            instance = new SingletonTracker();
        }
        return instance;
    }

    public void addExpense(String detail, double amount) {
        totals.put(detail, totals.getOrDefault(detail, 0.0) + amount);
    }

    public Map<String, Double> getTotals() {
        return totals;
    }
}
