package hichat.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String name;
    private String password;
    private ArrayList<String> friends;
    private Map<String, ArrayList<Message>> messages;
    
    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.friends = new ArrayList<>();
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
}
