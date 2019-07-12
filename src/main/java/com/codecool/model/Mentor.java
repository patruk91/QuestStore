package com.codecool.model;

public class Mentor extends User {
    protected Mentor(UserBuilder<?> builder) {
        super(builder);
    }

    public static class MentorBuilder extends User.UserBuilder<MentorBuilder> {
        public MentorBuilder() {}

        public Mentor build() {
            return new Mentor(this);
        }

        @Override
        MentorBuilder getThis() {
            return this;
        }
    }
}
