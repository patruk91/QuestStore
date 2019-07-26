package com.codecool.model;

public class ExpLevel {
    private String name;
    private int expAmountAtStart;
    private int expAmountAtEnd;

    public ExpLevel(String name, int expAmountAtStart, int expAmountAtEnd) {
        this.name = name;
        this.expAmountAtStart = expAmountAtStart;
        this.expAmountAtEnd = expAmountAtEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpAmountAtStart() {
        return expAmountAtStart;
    }

    public void setExpAmountAtStart(int expAmountAtStart) {
        this.expAmountAtStart = expAmountAtStart;
    }

    public int getExpAmountAtEnd() {
        return expAmountAtEnd;
    }

    public void setExpAmountAtEnd(int expAmountAtEnd) {
        this.expAmountAtEnd = expAmountAtEnd;
    }
}
