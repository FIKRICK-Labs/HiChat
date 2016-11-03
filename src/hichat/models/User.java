package hichat.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String name;
    private String password;
    private ArrayList<String> friends;
    private ArrayList<String> groups;
    private Map<String, ArrayList<Message>> messages;
    
    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.friends = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.messages = new HashMap<>();
    }
    
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void addFriends(String friend) {
        this.friends.add(friend);
    }
    
    public ArrayList<String> getFriends() {
        return this.friends;
    }
    
    public ArrayList<String> getGroups() {
        return this.groups;
    }
    public void addGroup(String newGroupName) {
        this.groups.add(newGroupName);
    }
    
    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
    }
    public ArrayList<Message> getMessages(String username) {
        return this.messages.get(username);
    }
    public void setMessages(String username, ArrayList<Message> message) {
        this.messages.put(username, message);
    }
    
    public void initFriends() {
        this.friends = new ArrayList<>();
    }
    
    public void initMessages() {
        this.messages = new HashMap<>();
    }
    
    public boolean doHaveGroup(String groupName) {
        return this.groups.contains(groupName);
    }
}
