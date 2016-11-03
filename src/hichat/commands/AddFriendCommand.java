package hichat.commands;

import hichat.models.CommandEnumeration;

public class AddFriendCommand extends Command {
    private String username;
    private String usernameTarget;
        
    public AddFriendCommand() {
        type = CommandEnumeration.ADDFRIEND.value();
    }
    
    public AddFriendCommand(String username, String usernameTarget) {
        type = CommandEnumeration.ADDFRIEND.value();
        
        this.username = username;
        this.usernameTarget = usernameTarget;
    }
    
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsernameTarget() {
        return this.usernameTarget;
    }
    public void setUsernameTarget(String usernameTarget) {
        this.usernameTarget = usernameTarget;
    }    
}
