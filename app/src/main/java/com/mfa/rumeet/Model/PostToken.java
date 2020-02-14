package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class PostToken {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    Token mToken;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Token getmToken() {
        return mToken;
    }

    public void setmToken(Token mToken) {
        this.mToken = mToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
