package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Ruangan {

    @SerializedName("id_ruangan")
    private String id;
    @SerializedName("nama_ruangan")
    private String nama;
    @SerializedName("kapasitas")
    private String kapasitas;
    @SerializedName("lokasi")
    private String lokasi;
    @SerializedName("keterangan_lain")
    private String keterangan_lain;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("status_ruangan")
    private String status;

    public Ruangan(String id,String nama, String gambar, String kapasitas, String lokasi, String keterangan) {
        this.id = id;
        this.nama = nama;
        this. gambar = gambar;
        this.kapasitas = kapasitas;
        this.lokasi = lokasi;
        this.keterangan_lain = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getKeterangan_lain() {
        return keterangan_lain;
    }

    public void setKeterangan_lain(String keterangan_lain) {
        this.keterangan_lain = keterangan_lain;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
