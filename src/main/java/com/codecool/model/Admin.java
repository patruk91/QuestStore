package com.codecool.model;

public class Admin extends User {
    protected Admin(UserBuilder<?> builder) {
        super(builder);
    }

    public static class AdminBuilder extends User.UserBuilder<AdminBuilder> {
        public AdminBuilder() {}

        public Admin build() {
            return new Admin(this);
        }

        @Override
        AdminBuilder getThis() {
            return this;
        }
    }
}
