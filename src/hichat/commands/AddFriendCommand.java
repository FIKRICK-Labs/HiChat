package hichat.commands;

import hichat.models.CommandEnumeration;

public class AddFriendCommand implements Command {
        private String username;
        private String usernameTarget;
    
    
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

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.ADD_FRIEND;
    }
    
}
