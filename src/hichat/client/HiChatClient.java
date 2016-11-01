package hichat.client;

import hichat.models.Notification;
import hichat.models.User;
import hichat.models.Group;
import java.util.ArrayList;
import java.util.Map;

public class HiChatClient {
    private User user;
    private Map<String, Group> groups;
    private ArrayList<Notification> notifications;
    private String messageExchange;
    private String RPCExchange;
    private String notificationExchange;


    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Group getGroups(String groupName) {
        return this.groups.get(groupName);
    }
    public void setGroups(Group group) {
        this.groups.put(group.getGroupName(), group);
    }
    public String getMessageExchange() {
        return this.messageExchange;
    }
    public void setMessageExchange(String messageExchange) {
        this.messageExchange = messageExchange;
    }
    public String getRPCExchange() {
        return this.RPCExchange;
    }
    public void setRPCExchange(String RPCExchange) {
        this.RPCExchange = RPCExchange;
    }
    public String getNotificationExchange() {
        return this.notificationExchange;
    }
    public void setNotificationExchange(String notificationExchange) {
        this.notificationExchange = notificationExchange;
    }
    
    //Operations                                  
    public void start() {
        //TODO
    }
    public void chat() {
        //TODO
    }
    public void chatGroup() {
        //TODO
    }
    public void execute() {
        //TODO
    }
    public boolean login() {
        //TODO
        return false;
    }
    public boolean register() {
        //TODO
        return false;
    }
    public String createGroup() {
        //TODO
        return "";
    }
    public boolean leaveGroup() {
        //TODO
        return false;
    }
    public boolean addFriend() {
        //TODO
        return false;
    }
    public boolean addGroupMember() {
        //TODO
        return false;
    }
    
    public static void main(String[] args) {
        
    }
    
}
