package hichat.controllers;

import hichat.models.Group;
import java.util.Map;

public class GroupManager {
    private Map<String, Group> groups;
    
    private Group getGroups(String groupName) {
        return this.groups.get(groupName);
    }
    
    private void setGroups(Group group) {
        this.groups.put(group.getGroupName(), group);
    }
    
    // Operations                                  
    public boolean removeMember() {
        //TODO
        return false;
    }
    
    public boolean addMember() {
        //TODO
        return false;
    }
    
    
}
