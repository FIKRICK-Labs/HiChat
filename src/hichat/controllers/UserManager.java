package hichat.controllers;

import hichat.models.User;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;
    
    public UserManager() {
        this.users = new HashMap<>();
    }
    
    public User getUser(String username) {
        return this.users.get(username);
    }
    
    public Map<String, User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        this.users.put(user.getUsername(), user);
    }
    
    // Operations                                  
    public boolean createFriendRelation(String usernameOne, String usernameTwo) {
        this.users.get(usernameOne).getFriends().add(usernameTwo);
        this.users.get(usernameTwo).getFriends().add(usernameOne);
        return true;
    }
    public boolean authenticateUser() {
        //TODO
        return false;
    }
    
    
}
