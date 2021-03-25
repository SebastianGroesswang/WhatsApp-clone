package org.acme.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
@Entity
@DiscriminatorValue(value = "USER")
public class UserMessage extends Message{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser room;

    public UserMessage(LocalDateTime timestamp, String value, AppUser user, AppUser room) {
        super(timestamp, value, user);
        this.room = room;
    }

    public UserMessage() {

    }

    public AppUser getRoom() {
        return room;
    }

    public void setRoom(AppUser room) {
        this.room = room;
    }
}
