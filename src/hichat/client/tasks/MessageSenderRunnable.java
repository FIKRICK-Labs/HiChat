/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.client.tasks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import hichat.models.Message;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import hichat.helpers.Helper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Chandra
 */
public class MessageSenderRunnable implements Runnable {
    private final String MESSAGE_EXCHANGE_NAME;
    private final String RABBITMQ_HOST;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    
    private final String destinationRoutingKey;
    private final Message message;
    
    public MessageSenderRunnable(String RABBITMQ_HOST, String MESSAGE_EXCHANGE_NAME, Message message, String recipientUsername) throws IOException, TimeoutException {
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.MESSAGE_EXCHANGE_NAME = MESSAGE_EXCHANGE_NAME;
        this.message = message;
        
        factory = new ConnectionFactory();
        factory.setHost(this.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        
        channel.exchangeDeclare(this.MESSAGE_EXCHANGE_NAME, "topic");
        
        destinationRoutingKey = "message." + recipientUsername;
    }

    @Override
    public void run() {
        try {
            channel.basicPublish(MESSAGE_EXCHANGE_NAME, destinationRoutingKey, null, Helper.serialize(this.message));
            System.out.println(" [x] Sent to topic '" + destinationRoutingKey + "' :: [Content:" + this.message.getContent() + "].");
        } catch (IOException ex) {
            Logger.getLogger(MessageSenderRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
