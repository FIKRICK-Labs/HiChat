package hichat.commands;

import hichat.models.CommandEnumeration;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateGroupCommand extends Command {
    private String groupName;
    private String admin;
    private ArrayList<String> members;
    
    public CreateGroupCommand() {
        this.members = new ArrayList<>();
        type = CommandEnumeration.CREATEGROUP.value();
    }     
    
    public CreateGroupCommand(String admin, String groupName, ArrayList<String> members) {
        type = CommandEnumeration.CREATEGROUP.value();
        
        this.admin = admin;
        this.groupName = groupName;
        this.members = members;
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
    public void setUsername(String member) {
        this.members.add(member);
    }
    public void addMember(String member) {
        this.members.add(member);
    }
    public ArrayList<String> getMembers() {
        return this.members;
    }
}
