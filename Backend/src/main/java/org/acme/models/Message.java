package org.acme.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long messageId;
    private LocalDateTime timestamp;
    private String value;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser user;

    public Message() {
    }

    public Message(LocalDateTime timestamp, String value, AppUser user) {
        this.timestamp = timestamp;
        this.user = user;
        this.value = value;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
