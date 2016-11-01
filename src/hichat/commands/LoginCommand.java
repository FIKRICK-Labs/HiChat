package hichat.commands;public class LoginCommand implements Command {
        private String username;
        private String password;
    
    
        private String getUsername() {
        return this.username;
    }
        private String setUsername(String username) {
        this.username = username;
    }
        private String getPassword() {
        return this.password;
    }
        private String setPassword(String password) {
        this.password = password;
    }
    
}
