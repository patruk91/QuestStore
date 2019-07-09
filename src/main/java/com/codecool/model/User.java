package com.codecool.model;

public class User {
    private int id;
    private String type;
    private UserCredentials userCredentials;
    private String firstName;
    private String lastName;
    private String email;

    private User() {}

    public static class UserBuilder {
        private User userToBuild;

        public UserBuilder() {
            this.userToBuild = new User();
        }

        public User build() {
            User buildUser = this.userToBuild;
            this.userToBuild = new User();
            return buildUser;
        }

        public UserBuilder setId(int id) {
            this.userToBuild.id = id;
            return this;
        }

        public UserBuilder setType(String type) {
            this.userToBuild.type = type;
            return this;
        }

        public UserBuilder setUserCredentials(UserCredentials userCredentials) {
            this.userToBuild.userCredentials = userCredentials;
            return this;
        }

        public UserBuilder setUserLogin(String login) {
            this.userToBuild.userCredentials.setLogin(login);
            return this;
        }

        public UserBuilder setUserPassword(String password) {
            this.userToBuild.userCredentials.setPassword(password);
            return this;
        }


        public UserBuilder setFirstName(String firstName) {
            this.userToBuild.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.userToBuild.lastName = lastName;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.userToBuild.email = email;
            return this;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogin() {
        return userCredentials.getLogin();
    }

    public String getPassword() {
        return userCredentials.getPassword();
    }

    public void setLogin(String login) {
        userCredentials.setLogin(login);
    }

    public void setPassword(String password) {
        userCredentials.setPassword(password);
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
