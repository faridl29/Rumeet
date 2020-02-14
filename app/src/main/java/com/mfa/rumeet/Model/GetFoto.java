package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFoto {

    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Foto_ruangan> foto_ruanganList;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Foto_ruangan> getFoto_ruanganList() {
        return foto_ruanganList;
    }

    public void setFoto_ruanganList(List<Foto_ruangan> foto_ruanganList) {
        this.foto_ruanganList = foto_ruanganList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
