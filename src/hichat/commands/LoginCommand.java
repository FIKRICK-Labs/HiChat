package hichat.commands;

import hichat.models.CommandEnumeration;

public class LoginCommand implements Command {
        private String username;
        private String password;
    
    
        private String getUsername() {
        return this.username;
    }
        private void setUsername(String username) {
        this.username = username;
    }
        private String getPassword() {
        return this.password;
    }
        private void setPassword(String password) {
        this.password = password;
    }

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.LOGIN;
    }
    
}
