package org.acme.models;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MembershipID implements Serializable {

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    public MembershipID(){}

    public MembershipID(Room room, AppUser user) {
        this.room = room;
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipID that = (MembershipID) o;
        return Objects.equals(room.getRoomId(), that.room.getRoomId()) &&
                Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, user);
    }
}
