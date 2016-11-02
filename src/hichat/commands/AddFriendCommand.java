package hichat.commands;

import hichat.models.CommandEnumeration;

public class AddFriendCommand extends Command {
    private String username;
    private String usernameTarget;
        
    public AddFriendCommand() {
        type = CommandEnumeration.ADDFRIEND.value();
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
