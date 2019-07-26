package com.codecool.model;

public class User {
    private int id;
    private String type;
    private UserCredentials userCredentials;
    private String firstName;
    private String lastName;
    private String email;

    protected User(UserBuilder<?> builder) {
        id = builder.id;
        type = builder.type;
        userCredentials = builder.userCredentials;
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
    }

    protected abstract static class UserBuilder<T extends  UserBuilder<T>> {
        private int id;
        private String type;
        private UserCredentials userCredentials;
        private String firstName;
        private String lastName;
        private String email;

        UserBuilder() {}

        public User build() {
            return new User(this);
        }

        abstract T getThis();

        public T setId(int id) {
            this.id = id;
            return getThis();
        }

        public T setType(String type) {
            this.type = type;
            return getThis();
        }

        public T setUserCredentials(UserCredentials userCredentials) {
            this.userCredentials = userCredentials;
            return getThis();
        }

        public T setFirstName(String firstName) {
            this.firstName = firstName;
            return getThis();
        }

        public T setLastName(String lastName) {
            this.lastName = lastName;
            return getThis();
        }

        public T setEmail(String email) {
            this.email = email;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", userCredentials=" + userCredentials +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
