package org.acme.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue(value = "ROOM")
public class RoomMessage extends Message{

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public RoomMessage() {
    }

    public RoomMessage(LocalDateTime timestamp, String value, AppUser user, Room room) {
        super(timestamp, value, user);
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
