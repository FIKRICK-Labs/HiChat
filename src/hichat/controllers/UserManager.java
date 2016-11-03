package hichat.controllers;

import hichat.commands.LoginCommand;
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
    
    public void removeGroupFromUser(String username, String groupName) {
        User user = this.users.get(username);
        user.removeGroup(groupName);
        
        this.users.replace(username, user);
    }
    
    // Operations                                  
    public boolean createFriendRelation(String usernameOne, String usernameTwo) {
        this.users.get(usernameOne).getFriends().add(usernameTwo);
        this.users.get(usernameTwo).getFriends().add(usernameOne);
        return true;
    }
    public boolean authenticateUser(LoginCommand loginCommand) {
        if (users.containsKey(loginCommand.getUsername())) {
            if (users.get(loginCommand.getUsername()).getPassword().equals(loginCommand.getPassword())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUserExist(String username) {
        return users.containsKey(username);
    }
    
    
}
