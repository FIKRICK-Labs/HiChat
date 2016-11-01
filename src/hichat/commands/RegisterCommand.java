package hichat.commands;public class RegisterCommand implements Command {
        private String username;
        private String password;
        private String name;
    
    
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
        private String getName() {
        return this.name;
    }
        private String setName(String name) {
        this.name = name;
    }
    
}
