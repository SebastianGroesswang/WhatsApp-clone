package org.acme.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Membership implements Serializable {

    @EmbeddedId
    private MembershipID id;

    public Membership() {
    }

    public Membership(MembershipID id) {
        this.id = id;
    }
}

