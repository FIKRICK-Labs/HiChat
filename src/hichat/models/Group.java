package hichat.models;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private String admin;
    private String members;
    private ArrayList<Message> messages;
    
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getAdmin() {
        return this.admin;
    }
    
    public void setAdmin(String admin) {
        this.admin = admin;
    }
    
    public String getMembers() {
        return this.members;
    }
    
    public void setMembers(String members) {
        this.members = members;
    }
    
    public ArrayList<Message> getMessages() {
        return this.messages;
    }
    
    public void setMessages(Message message) {
        this.messages.add(message);
    }
    
}
