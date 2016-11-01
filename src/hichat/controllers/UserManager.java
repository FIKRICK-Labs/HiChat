package hichat.controllers;

import hichat.models.User;

public class UserManager {
    private User users;
    
    
    private User getUsers() {
        return this.users;
    }
    private void setUsers(User users) {
        this.users = users;
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
