package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRuangan {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Ruangan> listRuangan;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Ruangan> getListRuangan() {
        return listRuangan;
    }

    public void setListRuangan(List<Ruangan> listRuangan) {
        this.listRuangan = listRuangan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
