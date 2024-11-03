// src/utils/ExpenseDecorator.java
package utils;

public abstract class ExpenseDecorator {
    protected String description;

    public ExpenseDecorator(String description) {
        this.description = description;
    }

    public abstract String getDescription();
}
