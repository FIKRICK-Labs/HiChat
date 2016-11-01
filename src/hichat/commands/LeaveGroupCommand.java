package hichat.commands;public class LeaveGroupCommand implements Command {
        private String groupName;
        private String username;
    
    
        private String getGroupName() {
        return this.groupName;
    }
        private String setGroupName(String groupName) {
        this.groupName = groupName;
    }
        private String getUsername() {
        return this.username;
    }
        private String setUsername(String username) {
        this.username = username;
    }
    
}
