package entities;

public class Type {
    private int typeID;
    private String name;
    private String description;

    public Type(int typeID, String name, String description) {
        this.typeID = typeID;
        this.name = name;
        this.description = description;
    }

    public int getTypeID() {
        return typeID;
    }

    @Override
    public String toString() {
        return name;
    }
}
