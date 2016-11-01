package hichat.client;

import hichat.commands.*;
import hichat.models.Notification;
import hichat.models.User;
import hichat.models.Group;
import hichat.models.Message;
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
    public Group getGroup(String groupName) {
        return this.groups.get(groupName);
    }
    public void addGroup(Group group) {
        this.groups.put(group.getGroupName(), group);
    }
    public void removeGroup(String groupName) {
        this.groups.remove(groupName);
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
    public void chat(Message message, String receiver) {
        //TODO
    }
    public void chatGroup(Message message, String groupName) {
        //TODO
    }
    public void execute() {
        //TODO
    }
    public void login(LoginCommand command) {
        //TODO
    }
    public void register(RegisterCommand command) {
        //TODO
    }
    public void createGroup(CreateGroupCommand command) {
        //TODO
    }
    public void leaveGroup(LeaveGroupCommand command) {
        //TODO
    }
    public void addFriend(AddFriendCommand command) {
        //TODO
    }
    public void addGroupMember(AddGroupMemberCommand command) {
        //TODO
    }
    
    public static void main(String[] args) {
        
    }
    
}
