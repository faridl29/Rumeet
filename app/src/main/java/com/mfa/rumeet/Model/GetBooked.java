package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBooked {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Booked> listBooked;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booked> getListBooked() {
        return listBooked;
    }

    public void setListBooked(List<Booked> listBooked) {
        this.listBooked = listBooked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
