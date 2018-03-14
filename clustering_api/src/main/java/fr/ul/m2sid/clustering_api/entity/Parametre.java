package fr.ul.m2sid.clustering_api.entity;

public class Parametre {
    private String name;
    private String value;

    public Parametre() {
    }

    public Parametre(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Parametre{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
