package hichat.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupName;
    private String admin;
    private ArrayList<String> members;
    private ArrayList<Message> messages;
    
    public Group() {
        members = new ArrayList<>();
        messages = new ArrayList<>();
    }
    
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
    
    public boolean isMemberExist(String member) {
        return this.members.contains(member);
    }
    
    public void addMember(String member) {
        this.members.add(member);
    }
    
    public void addMembers(ArrayList<String> members) {
        for(String member: members) {
            if(!this.members.contains(member)) {
                this.members.add(member);
            }
        }
    }
    
    public ArrayList<String> getMembers() {
        return this.members;
    }
    
    public void setMembers(ArrayList<String> arrayMembers) {
        this.members = arrayMembers;
    }
    
    public ArrayList<Message> getMessages() {
        return this.messages;
    }
    
    public void setMessages(Message message) {
        this.messages.add(message);
    }
    
    public void removeMember(String username) {
        this.members.remove(username);
    }
    
}
