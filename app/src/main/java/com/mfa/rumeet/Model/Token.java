package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("token")
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
