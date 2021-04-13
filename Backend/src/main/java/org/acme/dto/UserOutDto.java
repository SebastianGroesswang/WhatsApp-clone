package org.acme.dto;

public class UserOutDto {

    private String username;
    private Long id;

    public UserOutDto() {
    }

    public UserOutDto(Long id, String username) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
