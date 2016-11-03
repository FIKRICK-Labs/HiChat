/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.client.tasks;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import hichat.helpers.Helper;
import hichat.models.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Chandra
 */
public class MessageReceiverRunnable implements Runnable {
    
    private final String MESSAGE_EXCHANGE_NAME;
    private final String RABBITMQ_HOST;
    
    private String privateMessageRoutingKey;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private final String queueName;
    private Consumer consumer;
    
    private String username;
    private StringBuilder currentWindowUsername;
    private StringBuilder currentWindowGroupName;
    private Map<String, ArrayList<Message>> privateMessagesMap;
    private Map<String, ArrayList<Message>> groupMessagesMap;
    
    public MessageReceiverRunnable(String RABBITMQ_HOST, String MESSAGE_EXCHANGE_NAME, String username, StringBuilder currentWindowUsername, StringBuilder currentWindowGroupName, Map<String, ArrayList<Message>> privateMessagesMap, Map<String, ArrayList<Message>> groupMessagesMap) throws IOException, TimeoutException {
        this.MESSAGE_EXCHANGE_NAME = MESSAGE_EXCHANGE_NAME;
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        
        this.username = username;
        this.currentWindowUsername = currentWindowUsername;
        this.currentWindowGroupName = currentWindowGroupName;
        this.privateMessagesMap = privateMessagesMap;
        this.groupMessagesMap = groupMessagesMap;
        
        this.privateMessageRoutingKey = "#.private." + username;
        
        factory = new ConnectionFactory();
        factory.setHost(this.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        
        channel.exchangeDeclare(this.MESSAGE_EXCHANGE_NAME, "topic");
        queueName = channel.queueDeclare().getQueue();
    }
    
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void run() {
        System.out.println("Message receiver is running...");
        
        try {
            addNewBinding(privateMessageRoutingKey);
        } catch (IOException ex) {
            Logger.getLogger(hichat.server.tasks.MessageReceiverRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Message message = (Message) Helper.deserialize(body);
                    String[] routingKeySplit = envelope.getRoutingKey().split(".");
                    String receivedMessageType = routingKeySplit[routingKeySplit.length - 2];
                    if (receivedMessageType.equals("private")) {
                        if (currentWindowUsername.equals(message.getSender())) {
                            System.out.println(" [x] Received from topic '" + envelope.getRoutingKey() + "' :: [SENDER:" + message.getSender() + "], [CONTENT:" + message.getContent() + "], [DATE:" + message.getSentDate().toString() + "].");
                        }
                        else {
                            if (privateMessagesMap.containsKey(message.getSender())) {
                                privateMessagesMap.get(message.getSender()).add(message);
                            }
                            else {
                                ArrayList<Message> messagesList = new ArrayList<>();
                                messagesList.add(message);
                                privateMessagesMap.put(message.getSender(), messagesList);
                            }
                        }
                    }
                    else if (receivedMessageType.equals("group")) {
                        if (currentWindowGroupName.equals(message.getSender())) {
                            System.out.println(" [x] Received from topic '" + envelope.getRoutingKey() + "' :: [SENDER:" + message.getSender() + "], [CONTENT:" + message.getContent() + "], [DATE:" + message.getSentDate().toString() + "].");
                        }
                        else {
                            if (groupMessagesMap.containsKey(message.getSender())) {
                                groupMessagesMap.get(message.getSender()).add(message);
                            }
                            else {
                                ArrayList<Message> messagesList = new ArrayList<>();
                                messagesList.add(message);
                                groupMessagesMap.put(message.getSender(), messagesList);
                            }
                        }
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(hichat.server.tasks.MessageReceiverRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException ex) {
            Logger.getLogger(hichat.server.tasks.MessageReceiverRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addNewBinding(String newRoutingKey) throws IOException {
        channel.queueBind(queueName, MESSAGE_EXCHANGE_NAME, newRoutingKey);
    }
    
    public void addNewBindingUsername(String username) throws IOException {
        String newUsernameRoutingKey = "#.private." + username;
        channel.queueBind(queueName, MESSAGE_EXCHANGE_NAME, newUsernameRoutingKey);
    }
    
    public void addNewBindingGroupName(String groupName) throws IOException {
        String newUsernameRoutingKey = "#.group." + groupName;
        channel.queueBind(queueName, MESSAGE_EXCHANGE_NAME, newUsernameRoutingKey);
    }
    
}
