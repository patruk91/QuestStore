package com.codecool.model;

public class Mentor extends User {
    protected Mentor(UserBuilder<?> builder) {
        super(builder);
    }

    public static class MentorBuilder extends User.UserBuilder<Mentor.MentorBuilder> {
        private Mentor mentorToBuild;

        public MentorBuilder() {
            this.mentorToBuild = new Mentor(this);
        }

        public Mentor build() {
            Mentor buildUser = this.mentorToBuild;
            this.mentorToBuild = new Mentor(this);
            return buildUser;
        }
    }
}
