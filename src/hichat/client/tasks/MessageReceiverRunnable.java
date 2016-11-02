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
    
    private String privateMessageBindingKey;
    
    private final ConnectionFactory factory;
    private final Connection connection;
    private final Channel channel;
    private final String queueName;
    private Consumer consumer;
    
    public MessageReceiverRunnable(String RABBITMQ_HOST, String MESSAGE_EXCHANGE_NAME, String username) throws IOException, TimeoutException {
        this.MESSAGE_EXCHANGE_NAME = MESSAGE_EXCHANGE_NAME;
        this.RABBITMQ_HOST = MESSAGE_EXCHANGE_NAME;
        
        this.privateMessageBindingKey = "#." + username;
        
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
            addNewBinding(privateMessageBindingKey);
        } catch (IOException ex) {
            Logger.getLogger(hichat.server.tasks.MessageReceiverRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Message message = (Message) Helper.deserialize(body);
                    System.out.println(" [x] Received from topic '" + envelope.getRoutingKey() + "' :: [SENDER:" + message.getSender() + "], [CONTENT:" + message.getContent() + "], [DATE:" + message.getSentDate().toString() + "].");
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
    
    public void addNewBinding(String newBindingKey) throws IOException {
        channel.queueBind(queueName, MESSAGE_EXCHANGE_NAME, newBindingKey);
    }
    
}
