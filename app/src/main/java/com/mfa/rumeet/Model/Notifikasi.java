package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Notifikasi {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("messages")
    private String messages;
    @SerializedName("id_pengguna")
    private String id_pengguna;
    @SerializedName("waktu")
    private String waktu;
    @SerializedName("isClicked")
    private  String isClicked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIsClicked() {
        return isClicked;
    }

    public void setIsClicked(String isClicked) {
        this.isClicked = isClicked;
    }

}
