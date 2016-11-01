package hichat.server;

import hichat.controllers.GroupManager;
import hichat.controllers.UserManager;

public class HiChatServer {
    private GroupManager groupManager;
    private UserManager userManager;
    private String RPCExchange;
    private String messageExchange;
    private String notificationExchange;
    
    
    public GroupManager getGroupManager() {
        return this.groupManager;
    }
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
    public UserManager getUserManager() {
        return this.userManager;
    }
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public String getRPCExchange() {
        return this.RPCExchange;
    }
    public void setRPCExchange(String RPCExchange) {
        this.RPCExchange = RPCExchange;
    }
    public String getMessageExchange() {
        return this.messageExchange;
    }
    public void setMessageExchange(String messageExchange) {
        this.messageExchange = messageExchange;
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
    
    public static void main(String[] args) {
        
    }
    
}
