package hichat.commands;

import hichat.models.CommandEnumeration;

public class RegisterCommand extends Command {
    private String username;
    private String password;
    private String name;
    
    public RegisterCommand() {
        type = CommandEnumeration.REGISTER.value();
    }

    public RegisterCommand(String name, String username, String password) {
        type = CommandEnumeration.REGISTER.value();
        this.name = name;
        this.username = username;
        this.password = password;
    }
    
    public RegisterCommand(String username, String name, String password) {
        type = CommandEnumeration.REGISTER.value();
        this.username = username;
        this.name = name;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }    
}
