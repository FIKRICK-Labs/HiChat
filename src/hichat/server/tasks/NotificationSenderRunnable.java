/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hichat.server.tasks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import hichat.models.Notification;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import hichat.helpers.Helper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Chandra
 */
public class NotificationSenderRunnable implements Runnable {
    private final String NOTIFICATION_EXCHANGE_NAME;
    private final String RABBITMQ_HOST;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    
    private final Notification notification;
    private final String destinationDirectRoutingKey;
    
    public NotificationSenderRunnable(String RABBITMQ_HOST, String NOTIFICATION_EXCHANGE_NAME, String recipientUsername, Notification notification) throws IOException, TimeoutException {
        this.NOTIFICATION_EXCHANGE_NAME = NOTIFICATION_EXCHANGE_NAME;
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.notification = notification;
        
        factory = new ConnectionFactory();
        factory.setHost(this.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        
        channel.exchangeDeclare(this.NOTIFICATION_EXCHANGE_NAME, "direct");
        
        this.destinationDirectRoutingKey = "hichat_notification_" + recipientUsername;
    }

    @Override
    public void run() {
        try {
            channel.basicPublish(NOTIFICATION_EXCHANGE_NAME, destinationDirectRoutingKey, null, Helper.serialize(notification));
        } catch (IOException ex) {
            System.out.println(" [x] Sent to direct '" + destinationDirectRoutingKey + "' :: [Content:" + this.notification.getContent() + "].");
            Logger.getLogger(NotificationSenderRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
