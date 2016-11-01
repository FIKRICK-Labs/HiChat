package hichat.controllers;

import hichat.models.User;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;
    
    
    public User getUsers(String username) {
        return this.users.get(username);
    }
    public void setUsers(User user) {
        this.users.put(user.getUsername(), user);
    }
    
    // Operations                                  
    public boolean createFriendRelation() {
        //TODO
        return false;
    }
    public boolean authenticateUser() {
        //TODO
        return false;
    }
    
    
}
