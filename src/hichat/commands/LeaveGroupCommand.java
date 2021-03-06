package hichat.commands;

import hichat.models.CommandEnumeration;

public class LeaveGroupCommand extends Command {
    private String groupName;
    private String username;
    
    public LeaveGroupCommand() {
        type = CommandEnumeration.LEAVEGROUP.value();
    }
    
    public LeaveGroupCommand(String username, String groupName) {
        type = CommandEnumeration.LEAVEGROUP.value();
        this.groupName = groupName;
        this.username = username;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
