package org.acme.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MessageDto {
    private boolean isGroup;
    private String name;
    private String message;
    private Timestamp timestamp;
    private String username;

    public MessageDto() {
    }

    public MessageDto(boolean isGroup, String name, String message, Timestamp timestamp, String username) {
        this.isGroup = isGroup;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
