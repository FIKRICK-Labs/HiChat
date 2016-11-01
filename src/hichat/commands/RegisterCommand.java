package hichat.commands;

import hichat.models.CommandEnumeration;

public class RegisterCommand implements Command {
        private String username;
        private String password;
        private String name;
    
    
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
        private String getName() {
        return this.name;
    }
        private void setName(String name) {
        this.name = name;
    }

    @Override
    public CommandEnumeration getType() {
        return CommandEnumeration.REGISTER;
    }
    
}
