package hichat.server;

import hichat.controllers.GroupManager;
import hichat.controllers.UserManager;
import hichat.server.tasks.MessageReceiverRunnable;
import hichat.server.tasks.RPCServerRunnable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HiChatServer {
    
    private final String RABBITMQ_HOST = "localhost";
    private final String MESSAGE_EXCHANGE_NAME = "message_exchange";
    private final String RPC_EXCHANGE_NAME = "rpc_exchange";
    private final String RPC_QUEUE_NAME = "rpc_queue";
    
    private Thread messageReceiverThread;
    private Runnable messageReceiverRunnable;
    
    private Thread rpcServerThread;
    private Runnable rpcServerRunnable;
    
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
    public void start() throws IOException, TimeoutException {
        // Creating MessageReceiver
        messageReceiverRunnable = new MessageReceiverRunnable(RABBITMQ_HOST, MESSAGE_EXCHANGE_NAME);
        messageReceiverThread = new Thread(messageReceiverRunnable);
        messageReceiverThread.start();
        
        rpcServerRunnable = new RPCServerRunnable(RABBITMQ_HOST, RPC_EXCHANGE_NAME, RPC_QUEUE_NAME);
        rpcServerThread = new Thread(rpcServerRunnable);
        rpcServerThread.start();
    }
    
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("Creating server...");
        HiChatServer hiChatServer = new HiChatServer();
        hiChatServer.start();
    }
    
}
