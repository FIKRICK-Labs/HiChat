package hichat.commands;

import hichat.models.CommandEnumeration;
import java.util.ArrayList;
import java.util.Arrays;

public class AddGroupMemberCommand extends Command {
    private ArrayList<String> members;
    private String groupName;
    private String admin;
        
    public AddGroupMemberCommand() {
        this.members = new ArrayList<>();
        type = CommandEnumeration.ADDGROUPMEMBER.value();
    }
    public AddGroupMemberCommand(String admin, String groupName, String[] members) {
        type = CommandEnumeration.ADDGROUPMEMBER.value();
        
        this.admin = admin;
        this.groupName = groupName;
        this.members = new ArrayList<>(Arrays.asList(members));
    }
    public void setUsername(String username) {
        this.members.add(username);
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }    
}
