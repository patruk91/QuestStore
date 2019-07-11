package com.codecool.model;

public class StudentProfile {
    private int coins;
    private int experience;
    private int classId;

    public StudentProfile(int coins, int experience, int classId) {
        this.coins = coins;
        this.experience = experience;
        this.classId = classId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
