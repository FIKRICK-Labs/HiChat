package hichat.commands;

import hichat.models.CommandEnumeration;

public class CreateGroupCommand implements Command {
        private String groupName;
        private String usernameAdmin;
        private String username;
    
    
        private String getGroupName() {
        return this.groupName;
    }
        private void setGroupName(String groupName) {
        this.groupName = groupName;
    }
        private String getUsernameAdmin() {
        return this.usernameAdmin;
    }
        private void setUsernameAdmin(String usernameAdmin) {
        this.usernameAdmin = usernameAdmin;
    }
        private String getUsername() {
        return this.username;
    }
        private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.CREATE_GROUP;
    }
    
}
