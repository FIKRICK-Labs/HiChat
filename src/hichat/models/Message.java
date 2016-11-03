package hichat.models;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String sender;
    private Date sentDate;
    private String content;


    public String getSender() {
        return this.sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public Date getSentDate() {
        return this.sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
}
