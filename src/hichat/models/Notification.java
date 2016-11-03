package hichat.models;

import java.io.Serializable;

public class Notification implements Serializable {
    private NotificationEnumeration type;
    private String content;
    private Object object;

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
    
    public Object getObject() {
        return this.object;
    }
    
    public void setObject(Object object) {
        this.object = object;
    }
    
        //                          Operations                                  
    public void display() {
        //TODO
    }
}
