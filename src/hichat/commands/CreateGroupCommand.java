package hichat.commands;

import hichat.models.CommandEnumeration;

public class CreateGroupCommand extends Command {
    private String groupName;
    private String usernameAdmin;
    private String username;
    
    public CreateGroupCommand() {
        type = CommandEnumeration.CREATEGROUP.value();
    }     
    
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getUsernameAdmin() {
        return this.usernameAdmin;
    }
    public void setUsernameAdmin(String usernameAdmin) {
        this.usernameAdmin = usernameAdmin;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }    
}
