// src/utils/TagDecorator.java
package utils;

public class TagDecorator extends ExpenseDecorator {
    private String tag;

    public TagDecorator(String description, String tag) {
        super(description);
        this.tag = tag;
    }

    @Override
    public String getDescription() {
        return description + " [" + tag + "]";
    }
}
