package hichat.models;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String name;
    private String password;
    private String friends;
    private String msgQueueName;
    private String oprQueueName;
    private String notifQueueName;
    private Map<String, ArrayList<Message>> messages;
    
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
    public String getFriends() {
        return this.friends;
    }
    public void setFriends(String friends) {
        this.friends = friends;
    }
    public String getMsgQueueName() {
        return this.msgQueueName;
    }
    public void setMsgQueueName(String msgQueueName) {
        this.msgQueueName = msgQueueName;
    }
    public String getOprQueueName() {
        return this.oprQueueName;
    }
    public void setOprQueueName(String oprQueueName) {
        this.oprQueueName = oprQueueName;
    }
    public String getNotifQueueName() {
        return this.notifQueueName;
    }
    public void setNotifQueueName(String notifQueueName) {
        this.notifQueueName = notifQueueName;
    }
    public ArrayList<Message> getMessages(String username) {
        return this.messages.get(username);
    }
    public void setMessages(String username, ArrayList<Message> message) {
        this.messages.put(username, message);
    }
    
}
