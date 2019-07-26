package com.codecool.model;

public class Student extends User {
    private StudentProfile studentProfile;

    protected Student(StudentBuilder builder) {
        super(builder);
        studentProfile = builder.studentProfile;
    }

    public static class StudentBuilder extends User.UserBuilder<StudentBuilder> {
        private StudentProfile studentProfile;

        public StudentBuilder() {}

        public Student build() {
            return new Student(this);
        }

        @Override
        StudentBuilder getThis() {
            return this;
        }

        public StudentBuilder setStudentProfile(StudentProfile studentProfile) {
            this.studentProfile = studentProfile;
            return this;
        }
    }

    public int getClassId() {
        return this.studentProfile.getClassId();
    }

    public int getCoins() {
        return this.studentProfile.getCoins();
    }

    public int getExperience() {
        return this.studentProfile.getExperience();
    }

    public void setClassId(int classId) {
        this.studentProfile.setClassId(classId);
    }

    public void setCoins(int coins) {
        this.studentProfile.setCoins(coins);
    }

    public void setExperience(int experience) {
        this.studentProfile.setExperience(experience);
    }

    @Override
    public String toString() {
        return super.toString() + "Student{" +
                "studentProfile=" + studentProfile+
                '}';
    }
}
