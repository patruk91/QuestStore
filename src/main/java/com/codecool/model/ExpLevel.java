package com.codecool.model;

public class ExpLevel {
    private String name;
    private int expAmount;

    public ExpLevel(String name, int expAmount) {
        this.name = name;
        this.expAmount = expAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(int expAmount) {
        this.expAmount = expAmount;
    }
}
