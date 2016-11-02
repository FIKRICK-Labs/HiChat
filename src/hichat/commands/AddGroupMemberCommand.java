package hichat.commands;

import hichat.models.CommandEnumeration;

public class AddGroupMemberCommand extends Command {
    private String username;
    private String groupName;
        
    public AddGroupMemberCommand() {
        type = CommandEnumeration.ADDGROUPMEMBER.value();
    }    
    
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }    
}
