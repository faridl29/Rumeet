package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Foto_ruangan {
    @SerializedName("id")
    private String id;
    @SerializedName("nama_foto")
    private String nama_foto;
    @SerializedName("id_ruangan")
    private String id_ruangan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_foto() {
        return nama_foto;
    }

    public void setNama_foto(String nama_foto) {
        this.nama_foto = nama_foto;
    }

    public String getId_ruangan() {
        return id_ruangan;
    }

    public void setId_ruangan(String id_ruangan) {
        this.id_ruangan = id_ruangan;
    }
}
