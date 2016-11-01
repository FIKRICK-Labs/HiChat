package hichat.commands;

import hichat.models.CommandEnumeration;

public class AddGroupMemberCommand implements Command {
        private String username;
        private String groupName;
    
    
        private String getUsername() {
        return this.username;
    }
        private void setUsername(String username) {
        this.username = username;
    }
        private String getGroupName() {
        return this.groupName;
    }
        private void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.ADD_GROUP_MEMBER;
    }
    
}
