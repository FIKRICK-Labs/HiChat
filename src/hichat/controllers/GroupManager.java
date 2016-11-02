package hichat.controllers;

import hichat.models.Group;
import java.util.HashMap;
import java.util.Map;

public class GroupManager {
    private Map<String, Group> groups;
    
    public GroupManager() {
        this.groups = new HashMap<>();
    }
    
    public Group getGroups(String groupName) {
        return this.groups.get(groupName);
    }
    
    public void setGroups(Group group) {
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
