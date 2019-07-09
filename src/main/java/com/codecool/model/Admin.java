package com.codecool.model;

public class Admin extends User {
    protected Admin(UserBuilder<?> builder) {
        super(builder);
    }

    public static class AdminBuilder extends User.UserBuilder<AdminBuilder> {
        private Admin adminToBuild;

        public AdminBuilder() {
            this.adminToBuild = new Admin(this);
        }

        public Admin build() {
            Admin buildUser = this.adminToBuild;
            this.adminToBuild = new Admin(this);
            return buildUser;
        }
    }
}
