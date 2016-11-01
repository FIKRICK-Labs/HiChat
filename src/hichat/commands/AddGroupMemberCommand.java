package hichat.commands;public class AddGroupMemberCommand implements Command {
        private String username;
        private String groupName;
    
    
        private String getUsername() {
        return this.username;
    }
        private String setUsername(String username) {
        this.username = username;
    }
        private String getGroupName() {
        return this.groupName;
    }
        private String setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
}
