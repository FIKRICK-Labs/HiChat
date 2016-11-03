package hichat.client;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import hichat.client.tasks.MessageSenderRunnable;
import hichat.client.tasks.NotificationReceiverRunnable;
import hichat.commands.*;
import hichat.helpers.Helper;
import hichat.models.Notification;
import hichat.models.User;
import hichat.models.Group;
import hichat.models.Message;
import hichat.models.ResponseCommand;
import hichat.client.tasks.MessageReceiverRunnable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
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
    private LinkedList<Notification> notifications;
    private Helper helper;
    
    private final String RABBITMQ_HOST = "localhost";
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
    
    private Thread messageReceiverThread;
    private Thread notificationReceiverThread;
    private MessageReceiverRunnable messageReceiverRunnable;
    private NotificationReceiverRunnable notificationReceiverRunnable;
    
    private volatile StringBuilder chatWindowPrivateUsername = new StringBuilder();
    private volatile StringBuilder chatWindowGroupUsername = new StringBuilder();
    private volatile Map<String, ArrayList<Message>> incomingPrivateMessages = new HashMap<>();
    private volatile Map<String, ArrayList<Message>> incomingGroupMessages = new HashMap<>();

    public User getUser() {
        return this.user;
    }
    public void addNewFriend(String friend) {
        this.user.addFriends(friend);
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }
    public void displayNotification() {
        System.out.println("## Notification: ");
        for(Notification notification: this.notifications) {
            System.out.println("## " + notification.getContent());
        }
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
        
        notifications = new LinkedList<>();
    }
    
    public void close() throws Exception {
        connection.close();
        notificationReceiverThread.interrupt();
        messageReceiverThread.interrupt();
    }
    
    public void chat(Message message, String receiver) throws IOException, TimeoutException {
        MessageSenderRunnable messageSenderRunnable = new MessageSenderRunnable(RABBITMQ_HOST, MESSAGE_EXCHANGE_NAME, message, receiver, "private");
        messageSenderRunnable.run();
    }
    public void chatGroup(Message message, String groupName) throws IOException, TimeoutException {
        MessageSenderRunnable messageSenderRunnable = new MessageSenderRunnable(RABBITMQ_HOST, MESSAGE_EXCHANGE_NAME, message, groupName, "group");
        messageSenderRunnable.run();
    }
    
    public void login(LoginCommand command) throws IOException, InterruptedException, ClassNotFoundException, TimeoutException {
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
                System.out.println("LOGIN COMMAND RESPONSE: " +responseCommand.getStatus());
                if (responseCommand.getStatus().equals("SUCCESS")) {
                    HashMap<String, Object> objectMap = (HashMap<String, Object>) responseCommand.getObjectMap();
                    
                    if (objectMap.containsKey("user")) {
                        this.user = (User) objectMap.get("user");
                        messageReceiverRunnable = new MessageReceiverRunnable(RABBITMQ_HOST, MESSAGE_EXCHANGE_NAME, user.getUsername(), chatWindowPrivateUsername, chatWindowGroupUsername, incomingPrivateMessages, incomingGroupMessages);
                        messageReceiverThread = new Thread(messageReceiverRunnable);
                        messageReceiverThread.start();
                        
                        notificationReceiverRunnable = new NotificationReceiverRunnable(RABBITMQ_HOST, NOTIFICATION_EXCHANGE_NAME, user.getUsername(), this);
                        notificationReceiverThread = new Thread(notificationReceiverRunnable);
                        notificationReceiverThread.start();
                        
                        for (String friend : user.getFriends()) {
                            messageReceiverRunnable.addNewBindingUsername(friend);
                        }
                        for (String group : user.getGroups()) {
                            messageReceiverRunnable.addNewBindingGroupName(group);
                        }
                    }
                    printFriendList();
                    printGroupList();
                }
                
                break;
            }
        }
        
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
    public void createGroup(CreateGroupCommand command) throws IOException, InterruptedException, ClassNotFoundException {
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
                System.out.println("ADD FRIEND COMMAND RESPONSE: " +responseCommand.getStatus());
                
                if(responseCommand.getStatus().contains("SUCCESS")) {
                    this.user.addGroup(command.getGroupName());
                    
                    HashMap<String, Object> objectMap = (HashMap<String, Object>) responseCommand.getObjectMap();
                    if (objectMap.containsKey("group")) {
                        Group group = (Group) objectMap.get("group");
                        this.groups.put(command.getGroupName(), group);
                    }
                    
                } else {
                    System.out.println(responseCommand.getStatus());
                }
                break;
            }
        }
    }
    public void leaveGroup(LeaveGroupCommand command) throws IOException, InterruptedException, ClassNotFoundException {
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
                System.out.println("LEAVE GROUP COMMAND RESPONSE: " +responseCommand.getStatus());
                
                if(responseCommand.getStatus().contains("SUCCESS")) {
                    this.user.removeGroup(command.getGroupName());
                    this.groups.remove(command.getGroupName());
                } else {
                    System.out.println(responseCommand.getStatus());
                }
                break;
            }
        }
    }
    public void addFriend(AddFriendCommand command) throws IOException, InterruptedException, ClassNotFoundException {
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
                System.out.println("ADD FRIEND COMMAND RESPONSE: " +responseCommand.getStatus());
                
                if(responseCommand.getStatus().contains("SUCCESS")) {
                    this.user.addFriends(command.getUsernameTarget());
                } else {
                    System.out.println(responseCommand.getStatus());
                }
                break;
            }
        }
    }
    public void addGroupMember(AddGroupMemberCommand command) throws IOException, InterruptedException, ClassNotFoundException {
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
                System.out.println("ADD FRIEND COMMAND RESPONSE: " +responseCommand.getStatus());
                
                if(responseCommand.getStatus().contains("SUCCESS")) {
                    Group group = this.groups.get(command.getGroupName());
                    group.addMembers(command.getMembers());
                    
                    this.groups.replace(command.getGroupName(), group);
                } else {
                    System.out.println(responseCommand.getStatus());
                }
                break;
            }
        }
    }
    
    public void printFriendList() {
        System.out.println("Friend List");
        System.out.println("===========");
        for(String friend : user.getFriends()) {
            System.out.println(friend);
        }
    }
    
    public void printGroupList() {
        System.out.println("Group List");
        System.out.println("==========");
        for(String group : user.getGroups()) {
            System.out.println(group);
        }
    }
    
    public void chatWindow(String recipientName, String messageType) throws IOException, TimeoutException {
        System.out.println("===== CHAT WITH " + recipientName + " =====");
        if (messageType.equals("private")) {
            this.chatWindowPrivateUsername.setLength(recipientName.length() - 1);
            this.chatWindowPrivateUsername.replace(0, recipientName.length() - 1, recipientName);
            this.chatWindowPrivateUsername.trimToSize();
            
            if (this.incomingPrivateMessages.containsKey(recipientName)) {
                System.out.println("SFSDF");
                ListIterator<Message> iter = this.incomingPrivateMessages.get(recipientName).listIterator();
                while (iter.hasNext()) {
                    Message message = iter.next();
                    System.out.println(message.getSender() + " [" + message.getSentDate().toString() + "]: " + message.getContent());
                    iter.remove();
                }
            }
        }
        else if (messageType.equals("group")) {
            this.chatWindowGroupUsername.setLength(recipientName.length());
            this.chatWindowGroupUsername.replace(0, recipientName.length() - 1, recipientName);
            
            if (this.incomingGroupMessages.containsKey(recipientName)) {
                ListIterator<Message> iter = this.incomingGroupMessages.get(recipientName).listIterator();
                while (iter.hasNext()) {
                    Message message = iter.next();
                    System.out.println(message.getSender() + " [" + message.getSentDate().toString() + "]: " + message.getContent());
                    iter.remove();
                }
            }
        }
        
        Scanner reader = new Scanner(System.in);
        String readStr;
        do {
            readStr = reader.nextLine();
            if (readStr.equals("/EXITCHAT")) {
                break;
            }
            else {
                Message message = new Message();
                message.setSender(this.user.getUsername());
                message.setContent(readStr);
                message.setSentDate(new Date());
                if (messageType.equals("private")) {
                    this.chat(message, recipientName);
                }
                else if (messageType.equals("group")) {
                    this.chatGroup(message, recipientName);
                }
            }
        }
        while (true);
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
                            ArrayList<String> members = new ArrayList<>();
                            members.add(client.getUser().getUsername());
                            String input = reader.nextLine();
                            if(input != null && !input.equals("")) {
                                String[] membersArray = input.split("\\s+");
                                
                                members.addAll(Arrays.asList(membersArray));
                            }
                           
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
                            ArrayList<String> members = new ArrayList<>();
                            String input = reader.nextLine();
                            if(input != null && !input.equals("")) {
                                String[] membersArray = input.split("\\s+");
                                
                                members.addAll(Arrays.asList(membersArray));
                            }
                            
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
                        
                    case "CHATPRIVATE":
                        System.out.print(">> Input friend's username: ");
                        reader = new Scanner(System.in);
                        String inputRecipientName = reader.nextLine();
                        
                        if (client.getUser().getFriends().contains(inputRecipientName)) {
                            client.chatWindow(inputRecipientName, "private");
                        }
                        
                        else {
                            System.out.println("## Failed: friend not found.");
                        }
                        break;
                        
                    case "CHATGROUP":
                        System.out.print(">> Input group's username: ");
                        reader = new Scanner(System.in);
                        String inputRecipientGroupName = reader.nextLine();
                        if (client.getUser().getFriends().contains(inputRecipientGroupName)) {
                            client.chatWindow(inputRecipientGroupName, "private");
                        }
                        
                        else {
                            System.out.println("## Failed: group not found.");
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
                        
                    case "NOTIFICATION":
                        client.displayNotification();
                        break;

                    case "EXIT":

                        break mainloop;

                    default:
                        //Throw error
                        System.out.println("## Error: arguments is not completed");

                }

            } while(true);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(HiChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
