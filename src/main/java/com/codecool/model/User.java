package com.codecool.model;

public class User {
    private int id;
    private String type;
    private UserCredentials userCredentials;
    private String firstName;
    private String lastName;
    private String email;
    private StudentProfile studentProfile;

    protected User(UserBuilder<?> builder) {
        int id = builder.userToBuild.id;
        String type = builder.userToBuild.type;
        UserCredentials userCredentials = builder.userToBuild.userCredentials;
        String firstName = builder.userToBuild.firstName;
        String lastName = builder.userToBuild.lastName;
        String email = builder.userToBuild.email;
    }

    protected abstract static class UserBuilder<T extends  UserBuilder<T>> {
        private User userToBuild;

        UserBuilder() {
            this.userToBuild = new User(this);
        }

        public User build() {
            User buildUser = this.userToBuild;
            this.userToBuild = new User(this);
            return buildUser;
        }

        abstract T getThis();

        public T setId(int id) {
            this.userToBuild.id = id;
            return getThis();
        }

        public T setType(String type) {
            this.userToBuild.type = type;
            return getThis();
        }

        public T setUserCredentials(UserCredentials userCredentials) {
            this.userToBuild.userCredentials = userCredentials;
            return getThis();
        }

        public T setUserLogin(String login) {
            this.userToBuild.userCredentials.setLogin(login);
            return getThis();
        }

        public T setUserPassword(String password) {
            this.userToBuild.userCredentials.setPassword(password);
            return getThis();
        }

        public T setFirstName(String firstName) {
            this.userToBuild.firstName = firstName;
            return getThis();
        }

        public T setLastName(String lastName) {
            this.userToBuild.lastName = lastName;
            return getThis();
        }

        public T setEmail(String email) {
            this.userToBuild.email = email;
            return getThis();
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
