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
    
    private String getUsername() {
        return this.username;
    }
    private void setUsername(String username) {
        this.username = username;
    }
    private String getUsernameTarget() {
        return this.usernameTarget;
    }
    private void setUsernameTarget(String usernameTarget) {
        this.usernameTarget = usernameTarget;
    }    
}
