package com.shiep.jwt.pojo;

import lombok.Data;

/**
 * 载荷对象
 */
public class UserInfo {

    private Long id;

    private String username;

    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public UserInfo() {
    }

    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfo(Long id, String username, String photo) {
        this.id = id;
        this.username = username;
        this.photo = photo;
    }

}