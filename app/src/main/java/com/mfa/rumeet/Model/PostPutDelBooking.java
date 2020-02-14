package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class PostPutDelBooking {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    Ruangan mRuangan;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Ruangan getmRuangan() {
        return mRuangan;
    }

    public void setmRuangan(Ruangan mRuangan) {
        this.mRuangan = mRuangan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
