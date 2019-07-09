package com.codecool.model;

public class ExpLevel {
    private int id;
    private String className;
    private int mentorId;

    public ExpLevel(int id, String className, int mentorId) {
        this.id = id;
        this.className = className;
        this.mentorId = mentorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
}
