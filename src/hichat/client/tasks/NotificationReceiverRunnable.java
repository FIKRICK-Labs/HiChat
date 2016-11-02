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
import hichat.client.HiChatClient;
import hichat.helpers.Helper;
import hichat.models.Message;
import hichat.models.Notification;
import hichat.models.NotificationEnumeration;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Chandra
 */
public class NotificationReceiverRunnable implements Runnable {
    private final String NOTIFICATION_EXCHANGE_NAME;
    private final String RABBITMQ_HOST;
    
    private String notificationDirectRoutingKey;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private final String queueName;
    private Consumer consumer;
    
    private final HiChatClient hiChatClient;
    
    public NotificationReceiverRunnable(String RABBITMQ_HOST, String NOTIFICATION_EXCHANGE_NAME, String username, HiChatClient hiChatClient) throws IOException, TimeoutException {
        this.RABBITMQ_HOST = RABBITMQ_HOST;
        this.NOTIFICATION_EXCHANGE_NAME = NOTIFICATION_EXCHANGE_NAME;
        this.notificationDirectRoutingKey = "hichat_notification_" + username;
        this.hiChatClient = hiChatClient;
        
        factory = new ConnectionFactory();
        factory.setHost(this.RABBITMQ_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        
        channel.exchangeDeclare(this.NOTIFICATION_EXCHANGE_NAME, "direct");
        queueName = channel.queueDeclare().getQueue();
        
        channel.queueBind(queueName, this.NOTIFICATION_EXCHANGE_NAME, this.notificationDirectRoutingKey);
    }

    @Override
    public void run() {
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Notification notification = (Notification) Helper.deserialize(body);
                    System.out.println(" [x] Received from topic '" + envelope.getRoutingKey() + "' :: [CONTENT:" + notification.getContent() + "].");
                    
                    // Processing Notification
                    if (notification.getType().equals(NotificationEnumeration.SOMEONE_ADDED_YOU)) {
                        
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(hichat.server.tasks.MessageReceiverRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }
    
}
