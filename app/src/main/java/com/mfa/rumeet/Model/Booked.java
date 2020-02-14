package com.mfa.rumeet.Model;

import com.google.gson.annotations.SerializedName;

public class Booked {
    @SerializedName("id_book")
    private String id;
    @SerializedName("nama_pengguna")
    private String nama;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("dari_jam")
    private String dari_jam;
    @SerializedName("sampai_jam")
    private String sampai_jam;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("status_booking")
    private String status;
    @SerializedName("id_ruangan")
    private String id_ruangan;
    @SerializedName("nama_ruangan")
    private String nama_ruangan;
    @SerializedName("timer")
    private String timer;

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDari_jam() {
        return dari_jam;
    }

    public void setDari_jam(String dari_jam) {
        this.dari_jam = dari_jam;
    }

    public String getSampai_jam() {
        return sampai_jam;
    }

    public void setSampai_jam(String sampai_jam) {
        this.sampai_jam = sampai_jam;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_ruangan() {
        return id_ruangan;
    }

    public void setId_ruangan(String id_ruangan) {
        this.id_ruangan = id_ruangan;
    }

    public String getNama_ruangan() {
        return nama_ruangan;
    }

    public void setNama_ruangan(String nama_ruangan) {
        this.nama_ruangan = nama_ruangan;
    }
}
