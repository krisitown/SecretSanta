package bg.sofia.uni.fmi.mjt.wish.list.services;

import bg.sofia.uni.fmi.mjt.wish.list.models.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class UserService {
    private Map<String, User> registeredUsers;
    private User principal;

    public UserService() {
        this.registeredUsers = new HashMap<>();
    }

    public UserService(Map<String, User> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public String register(String username, String password){
        try {
            User user = new User(username, password);
            if(this.registeredUsers.containsKey(username)){
                return String.format("Username %s is already taken, select another one", username);
            }

            this.registeredUsers.put(username, user);
            this.principal = user;
            return String.format("Username %s successfully registered", username);
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }
    }

    public String login(String username, String password){
        if(this.registeredUsers.containsKey(username)){
            User registered = this.registeredUsers.get(username);
            if(registered.getPassword().equals(password)) {
                this.principal = registered;
                return String.format("User %s successfully logged in", username);
            }
        }

        return "Invalid username/password combination";
    }

    public String logout() {
        if(this.principal == null) return "You are not logged in";
        this.principal = null;
        return "Successfully logged out";
    }

    public User getPrincipal() {
        return this.principal;
    }

    public Set<String> getRegisteredUsernames(){
        return this.registeredUsers.keySet();
    }

}
