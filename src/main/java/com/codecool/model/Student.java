package com.codecool.model;

public class Student extends User {
    private StudentProfile studentProfile;

    protected Student(UserBuilder<?> builder) {
        super(builder);
    }

    public static class StudentBuilder extends User.UserBuilder<StudentBuilder> {
        private Student studentToBuild;

        public StudentBuilder() {
            this.studentToBuild =  new Student(this);
        }

        public Student build() {
            Student buildUser = this.studentToBuild;
            this.studentToBuild = new Student(this);
            return buildUser;
        }

        @Override
        StudentBuilder getThis() {
            return this;
        }

        public StudentBuilder setId(int classId) {
            this.studentToBuild.studentProfile.setClassId(classId);
            return this;
        }

        public StudentBuilder setCoins(int coins) {
            this.studentToBuild.studentProfile.setCoins(coins);
            return this;
        }

        public StudentBuilder setExperience(int experience) {
            this.studentToBuild.studentProfile.setExperience(experience);
            return this;
        }
    }

    public int getId() {
        return this.studentProfile.getClassId();
    }

    public int getCoins() {
        return this.studentProfile.getCoins();
    }

    public int getExperience() {
        return this.studentProfile.getExperience();
    }

    public void setId(int classId) {
        this.studentProfile.setClassId(classId);
    }

    public void setCoins(int coins) {
        this.studentProfile.setCoins(coins);
    }

    public void setExperience(int experience) {
        this.studentProfile.setExperience(experience);
    }

}
