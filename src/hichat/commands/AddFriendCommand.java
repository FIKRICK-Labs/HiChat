package hichat.commands;public class AddFriendCommand implements Command, Command {
        private String username;
        private String usernameTarget;
    
    
        private String getUsername() {
        return this.username;
    }
        private String setUsername(String username) {
        this.username = username;
    }
        private String getUsernameTarget() {
        return this.usernameTarget;
    }
        private String setUsernameTarget(String usernameTarget) {
        this.usernameTarget = usernameTarget;
    }
    
}
