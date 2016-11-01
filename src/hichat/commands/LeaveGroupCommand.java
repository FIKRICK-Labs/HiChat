package hichat.commands;

import hichat.models.CommandEnumeration;

public class LeaveGroupCommand implements Command {
        private String groupName;
        private String username;
    
    
        private String getGroupName() {
        return this.groupName;
    }
        private void setGroupName(String groupName) {
        this.groupName = groupName;
    }
        private String getUsername() {
        return this.username;
    }
        private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.LEAVE_GROUP;
    }
    
}
