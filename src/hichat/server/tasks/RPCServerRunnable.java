/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.server.tasks;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import hichat.commands.*;
import hichat.controllers.GroupManager;
import hichat.controllers.UserManager;
import hichat.helpers.Helper;
import hichat.models.Group;
import hichat.models.ResponseCommand;
import hichat.models.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Chandra
 */
public class RPCServerRunnable implements Runnable {
    private final String RPC_EXCHANGE_NAME;
    private final String RPC_QUEUE_NAME;
    private final String RABBITMQ_HOST;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private final QueueingConsumer consumer;
    
    private final UserManager userManager;
    private final GroupManager groupManager;
    
    public RPCServerRunnable(String RABBITMQ_HOST, String RPC_EXCHANGE_NAME, String RPC_QUEUE_NAME, UserManager userManager, GroupManager groupManager) throws IOException, TimeoutException {
        this.RPC_EXCHANGE_NAME = RPC_EXCHANGE_NAME;
        this.RPC_QUEUE_NAME = RPC_QUEUE_NAME;
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.userManager = userManager;
        this.groupManager = groupManager;
        
        factory = new ConnectionFactory();
        factory.setHost(this.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        
        channel.basicQos(1);
        
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        
        System.out.println(" [x] Awaiting RPC requests");
    }

    @Override
    public void run() {
        while (true) {
            String response = null;
            ResponseCommand responseCommand = null;
            try {

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
//                    String message = new String(delivery.getBody(),"UTF-8");
                    Command command = (Command) Helper.deserialize(delivery.getBody());
    //                int n = Integer.parseInt(message);
                    String status;
                    Map<String, Object> objectMap = new HashMap<>();
                    switch (command.getType()) {
                        case "ADDFRIEND":
                            AddFriendCommand addFriendCommand = (AddFriendCommand) command;
                            String username = addFriendCommand.getUsername();
                            String usernameTarget = addFriendCommand.getUsernameTarget();
                            
                            if (userManager.isUserExist(username) && userManager.isUserExist(usernameTarget)) {
                               responseCommand = new ResponseCommand("SUCCESS");
                               userManager.createFriendRelation(username, usernameTarget);
                                
                                //Send Notification to target user
                            } else {
                                responseCommand = new ResponseCommand("FAILED: User does not exist.");
                            }
                            
                            break;
                        case "LEAVEGROUP":
                            LeaveGroupCommand leaveGroupCommand = (LeaveGroupCommand) command;
                            
                            if(this.userManager.getUser(leaveGroupCommand.getUsername()).doHaveGroup(leaveGroupCommand.getGroupName())) {
                                this.userManager.removeGroupFromUser(leaveGroupCommand.getUsername(), leaveGroupCommand.getGroupName());
                                this.groupManager.removeGroup(leaveGroupCommand.getGroupName());
                                responseCommand = new ResponseCommand("SUCCESS");
                            } else {
                                responseCommand = new ResponseCommand("FAILED: User is not member of Group " + leaveGroupCommand.getGroupName() + ".");
                            }
                            
                            break;
                        case "LOGIN":
                            LoginCommand loginCommand = (LoginCommand) command;
                            if (userManager.authenticateUser(loginCommand)) {
                                responseCommand = new ResponseCommand();
                                responseCommand.setStatus("SUCCESS");
                                responseCommand.addObjectMap("user", userManager.getUser(loginCommand.getUsername()));
                            }
                            else {
                                responseCommand = new ResponseCommand("FAILED");
                            }
                            
                            break;
                        case "REGISTER":
                            RegisterCommand registerCommand = (RegisterCommand) command;
                            if (userManager.getUsers().containsKey(registerCommand.getUsername())) {
                                status = "FAILED: Username exists.";
                            }
                            else {
                                User newUser = new User(registerCommand.getUsername(), registerCommand.getName(), registerCommand.getPassword());
                                userManager.addUser(newUser);
                                status = "SUCCESS";
                            }
                            responseCommand = new ResponseCommand(status);
                            break;
                        case "CREATEGROUP":
                            CreateGroupCommand createGroupCommand = (CreateGroupCommand) command;
                            if (this.groupManager.isGroupExist(createGroupCommand.getGroupName())) {                                
                                Group group = new Group();
                                group.setAdmin(createGroupCommand.getAdmin());
                                group.setGroupName(createGroupCommand.getGroupName());
                                for(String member: createGroupCommand.getMembers()) {
                                    if(this.userManager.isUserExist(member)) {
                                        group.addMember(member);
                                    }
                                }
                                this.groupManager.addGroup(group);
                                
                                responseCommand = new ResponseCommand();
                                responseCommand.setStatus("SUCCESS");
                                responseCommand.addObjectMap("group", group);
                            } else {
                                responseCommand = new ResponseCommand("FAILED: Group does exist.");
                            }
                                
                            break;
                        case "ADDGROUPMEMBER":
                            AddGroupMemberCommand addGroupMemberCommand = (AddGroupMemberCommand) command;
                            if(this.groupManager.isGroupExist(addGroupMemberCommand.getGroupName())) {
                                responseCommand = new ResponseCommand("SUCCESS");
                                
                                Group group = this.groupManager.getGroup(addGroupMemberCommand.getGroupName());
                                
                                group.addMembers(addGroupMemberCommand.getMembers());
                                
                                this.groupManager.replaceGroup(addGroupMemberCommand.getGroupName(), group);
                            } else {
                                responseCommand = new ResponseCommand("FAILED: Group does not exist.");                                
                            }
                            break;
                        default:
                            responseCommand = new ResponseCommand("FAILED: System does not recognize operation.");
                            break;
                    }
                    
                    System.out.println(" [.] Received command: " + command.getType());

//                    System.out.println(" [.] fib(" + message + ")");
    //                response = "" + fib(n);
                }
                catch (Exception e){
                    System.out.println(" [.] " + e.toString());
                    response = "";
                }
                finally {
                    try {
                        channel.basicPublish(this.RPC_EXCHANGE_NAME, props.getReplyTo(), replyProps, Helper.serialize(responseCommand));

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(RPCServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RPCServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            catch (InterruptedException ex){
                    Logger.getLogger(RPCServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ShutdownSignalException ex) {
                Logger.getLogger(RPCServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ConsumerCancelledException ex) {
                Logger.getLogger(RPCServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
