package bg.sofia.uni.fmi.mjt.wish.list.models;

import java.util.Objects;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.setUsername(username);
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(!username.matches("[A-Za-z0-9_.\\-]+")){
            throw new IllegalArgumentException(String.format("Username %s is invalid, select a valid one", username));
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
