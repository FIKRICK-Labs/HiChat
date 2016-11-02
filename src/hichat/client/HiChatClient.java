package hichat.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import hichat.commands.*;
import hichat.helpers.Helper;
import hichat.models.Notification;
import hichat.models.User;
import hichat.models.Group;
import hichat.models.Message;
import hichat.models.ResponseCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiChatClient {
    private User user;
    private Map<String, Group> groups;
    private ArrayList<Notification> notifications;
    private Helper helper;
    
    private final String MESSAGE_EXCHANGE_NAME = "message_exchange";
    private final String RPC_EXCHANGE_NAME = "";
    private final String NOTIFICATION_EXCHANGE_NAME = "notification_exchange";
    
    private final String RPC_QUEUE_NAME = "rpc_queue";
    private String oprQueueName;
    private String msgQueueName;
    private String notifQueueName;
    
    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;

    private static Queue<String> listOfActions = new LinkedList<>();

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
        return this.MESSAGE_EXCHANGE_NAME;
    }
    public String getRPCExchange() {
        return this.RPC_EXCHANGE_NAME;
    }
    public String getNotificationExchange() {
        return this.NOTIFICATION_EXCHANGE_NAME;
    }
    
    public String getMsgQueueName() {
        return this.msgQueueName;
    }
    public void setMsgQueueName(String msgQueueName) {
        this.msgQueueName = msgQueueName;
    }
    public String getOprQueueName() {
        return this.oprQueueName;
    }
    public void setOprQueueName(String oprQueueName) {
        this.oprQueueName = oprQueueName;
    }
    public String getNotifQueueName() {
        return this.notifQueueName;
    }
    public void setNotifQueueName(String notifQueueName) {
        this.notifQueueName = notifQueueName;
    }
    
    //Operations                                  
    public HiChatClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        oprQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(oprQueueName, true, consumer);       
       
    }
    
    public void close() throws Exception {
        connection.close();
    }
    
    public void chat(Message message, String receiver) {
        //TODO
    }
    public void chatGroup(Message message, String groupName) {
        //TODO
    }
    
    public void login(LoginCommand command) throws IOException, InterruptedException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
        
        String response;
        
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(), "UTF-8");
                break;
            }
        }
        
        System.out.println(response);
    }
    public void register(RegisterCommand command) throws IOException, InterruptedException, ClassNotFoundException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
        
        while (true) {
            ResponseCommand responseCommand;
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                responseCommand = (ResponseCommand) Helper.deserialize(delivery.getBody());
                System.out.println("REGISTER COMMAND RESPONSE: " +responseCommand.getStatus());
                break;
            }
        }
    }
    public void createGroup(CreateGroupCommand command) throws IOException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
        
    }
    public void leaveGroup(LeaveGroupCommand command) throws IOException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
    }
    public void addFriend(AddFriendCommand command) throws IOException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
    }
    public void addGroupMember(AddGroupMemberCommand command) throws IOException {
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                                    .Builder()
                                    .correlationId(corrId)
                                    .replyTo(oprQueueName)
                                    .build();
        
        channel.basicPublish(this.RPC_EXCHANGE_NAME, this.RPC_QUEUE_NAME, props, Helper.serialize(command));
    }
    
    public static void main(String[] args) throws IOException, TimeoutException, ClassNotFoundException {
        HiChatClient client =  new HiChatClient();
        
        try {
            mainloop:
            do {
                System.out.print(">> ");
                Scanner reader = new Scanner(System.in);
                listOfActions.add(reader.nextLine());

                String[] splitStr = listOfActions.remove().split("\\s+");
                switch (splitStr[0].toUpperCase()) {
                    case "LOGIN":
                        if (splitStr.length >= 1) {
                            reader = new Scanner(System.in);
                            
                            System.out.println("========== Login Information ==========");
                            System.out.print(">> Username: ");
                            String username = reader.nextLine();
                            System.out.print(">> Password: ");
                            String password = reader.nextLine();
                            
                            client.login(new LoginCommand(username, password));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "REGISTER":
                        if (splitStr.length >= 1) {
                            reader = new Scanner(System.in);
                            
                            System.out.println("========== Register Information ==========");
                            System.out.print(">> Name: ");
                            String name = reader.nextLine();
                            System.out.print(">> Username: ");
                            String username = reader.nextLine();
                            System.out.print(">> Password: ");
                            String password = reader.nextLine();

                        client.register(new RegisterCommand(name, username, password));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "ADDFRIEND":
                        if (splitStr.length >= 2) {
                            reader = new Scanner(System.in);
                            client.addFriend(new AddFriendCommand(client.getUser().getUsername(), splitStr[1]));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "CREATEGROUP":
                        if (splitStr.length >= 1) {
                            reader = new Scanner(System.in);
                            System.out.println("========== Group Information ==========");
                            System.out.print(">> Group Name: ");
                            String name = reader.nextLine();
                            System.out.print(">> Members[separate by space]: ");
                            String[] members = reader.nextLine().split("\\s+");
                            
                            client.createGroup(new CreateGroupCommand(client.getUser().getUsername(), name, members));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "ADDGROUPMEMBER":
                        if (splitStr.length >= 1) {
                            reader = new Scanner(System.in);
                            System.out.println("========== Add Group Members Information ==========");
                            System.out.print(">> Group Name: ");
                            String groupName = reader.nextLine();
                            System.out.print(">> Members[separate by space]: ");
                            String[] members = reader.nextLine().split("\\s+");
                            
                            client.addGroupMember(new AddGroupMemberCommand(client.getUser().getUsername(), groupName, members));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "LEAVEGROUP":
                        if (splitStr.length >= 2) {
                            reader = new Scanner(System.in);
                            client.leaveGroup(new LeaveGroupCommand(client.getUser().getUsername(), splitStr[1]));

                        } else {
                            System.out.println("## Error: arguments is not completed");
                        }
                        break;

                    case "HELP":
                        System.out.println(">> ========== Command Information ==========");
                        System.out.println(">> 1. LOGIN");
                        System.out.println(">> 2. REGISTER");
                        System.out.println(">> 3. ADDFRIEND [USERNAME]");
                        System.out.println(">> 4. CREATEGROUP");
                        System.out.println(">> 5. ADDGROUPMEMBER");
                        System.out.println(">> 6. LEAVEGROUP [GROUPNAME]");
                        
                        break;

                    case "EXIT":

                        break mainloop;

                    default:
                        //Throw error

                }

            } while(true);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(HiChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
