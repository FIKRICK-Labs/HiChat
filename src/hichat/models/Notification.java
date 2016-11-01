package hichat.models;

public class Notification {
    private NotificationEnumeration type;
    private String content;


    public NotificationEnumeration getType() {
        return this.type;
    }
    
    public void setType(NotificationEnumeration type) {
        this.type = type;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
        //                          Operations                                  
    public void display() {
        //TODO
    }
    
    
}
