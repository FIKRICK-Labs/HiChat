package hichat.controllers;

import hichat.models.Group;
import java.util.HashMap;
import java.util.Map;

public class GroupManager {
    private Map<String, Group> groups;
    
    public GroupManager() {
        this.groups = new HashMap<>();
    }
    
    public Group getGroup(String groupName) {
        return this.groups.get(groupName);
    }
    
    public void addGroup(Group group) {
        this.groups.putIfAbsent(group.getGroupName(), group);
    }
    
    public boolean isGroupExist(String groupName) {
        return this.groups.containsKey(groupName);
    }

    public void replaceGroup(String groupName, Group group) {
        this.groups.replace(groupName, group);
    }
    
    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
    }
    
}
