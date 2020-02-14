package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Fasilitas {

    @SerializedName("icon")
    private String icon;
    @SerializedName("nama_fasilitas")
    private String nama_fasilitas;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNama_fasilitas() {
        return nama_fasilitas;
    }

    public void setNama_fasilitas(String nama_fasilitas) {
        this.nama_fasilitas = nama_fasilitas;
    }
}
