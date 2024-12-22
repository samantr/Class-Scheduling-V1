package com.esenyurt.entity;

public class Subject {

    public int id;
    public String name;
    public boolean isPractical;
    public int units;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPractical() {
        return isPractical;
    }

    public void setPractical(boolean practical) {
        isPractical = practical;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

}
