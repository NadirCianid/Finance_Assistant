package entities;

public class Category {
    private int categoryID;

    private String name;
    private String description;

    public Category(int categoryID, String name, String description) {
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
    }

    public int getCategoryID() {
        return categoryID;
    }

    @Override
    public String toString() {
        return name;
    }
}
