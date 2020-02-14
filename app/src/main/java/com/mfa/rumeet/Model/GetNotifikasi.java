package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNotifikasi {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Notifikasi> listNotifikasi;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Notifikasi> getListNotifikasi() {
        return listNotifikasi;
    }

    public void setListNotifikasi(List<Notifikasi> listNotifikasi) {
        this.listNotifikasi = listNotifikasi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
