package com.example.movie.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserSessionDto {
    @JsonIgnore
    private int id;
    private String username;
    private String image;
    private Boolean authenication;
    private String token;
    private Boolean authorize;
    private String refreshToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAuthenication() {
        return authenication;
    }

    public void setAuthenication(Boolean authenication) {
        this.authenication = authenication;
    }

    public Boolean getAuthorize() {
        return authorize;
    }

    public void setAuthorize(Boolean authorize) {
        this.authorize = authorize;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

}
