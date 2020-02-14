package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFasilitas {

    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Fasilitas> listFasilitas;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Fasilitas> getListFasilitas() {
        return listFasilitas;
    }

    public void setListFasilitas(List<Fasilitas> listFasilitas) {
        this.listFasilitas = listFasilitas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

