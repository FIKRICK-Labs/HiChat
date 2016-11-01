package hichat.commands;public class CreateGroupCommand implements Command {
        private String groupName;
        private String usernameAdmin;
        private String username;
    
    
        private String getGroupName() {
        return this.groupName;
    }
        private String setGroupName(String groupName) {
        this.groupName = groupName;
    }
        private String getUsernameAdmin() {
        return this.usernameAdmin;
    }
        private String setUsernameAdmin(String usernameAdmin) {
        this.usernameAdmin = usernameAdmin;
    }
        private String getUsername() {
        return this.username;
    }
        private String setUsername(String username) {
        this.username = username;
    }
    
}
