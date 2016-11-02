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
import hichat.commands.Command;
import hichat.commands.LoginCommand;
import hichat.commands.RegisterCommand;
import hichat.controllers.GroupManager;
import hichat.controllers.UserManager;
import hichat.helpers.Helper;
import hichat.models.CommandEnumeration;
import hichat.models.ResponseCommand;
import hichat.models.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
                            response = "Add Friend";
                            break;
                        case "LEAVEGROUP":
                            response = "Leave Group";
                            break;
                        case "LOGIN":
                            LoginCommand loginCommand = (LoginCommand) command;
                            if (userManager.authenticateUser(loginCommand)) {
                                responseCommand = new ResponseCommand();
                                responseCommand.setStatus("SUCCESS.");
                                responseCommand.addObjectMap("user", userManager.getUser(loginCommand.getUsername()));
                            }
                            else {
                                responseCommand = new ResponseCommand("FAILED.");
                            }
                            
                            break;
                        case "REGISTER":
                            RegisterCommand registerCommand = (RegisterCommand) command;
                            if (userManager.getUsers().containsKey(registerCommand.getUsername())) {
                                status = "FAILED. Username exists.";
                            }
                            else {
                                User newUser = new User(registerCommand.getUsername(), registerCommand.getName(), registerCommand.getPassword());
                                userManager.addUser(newUser);
                                status = "SUCCESS.";
                            }
                            responseCommand = new ResponseCommand(status);
                            break;
                        case "CREATEGROUP":
                            response = "Create Group";
                            break;
                        case "ADDGROUPMEMBER":
                            response = "Add Group Member";
                            break;
                        default:
                            response = "ERROR";
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
