package hichat.commands;

import hichat.models.CommandEnumeration;

public class LoginCommand extends Command {
    private String username;
    private String password;
    
    public LoginCommand() {
        type = CommandEnumeration.LOGIN.value();
    }
    
    public LoginCommand(String username, String password) {
        type = CommandEnumeration.LOGIN.value();
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }    
}
