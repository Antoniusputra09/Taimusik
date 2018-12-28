package com.example.asus.taimusik;

public class UserIItems {

    private String nama_user;
    private String tanggal;
    private String fotopp;
    private String kota;
    private String nama_asli;

    public String getFotopp() {
        return fotopp;
    }

    public void setFotopp(String imgUrl) {
        this.fotopp = imgUrl;
    }

    public String getNama_user() {
        return nama_user;
    }


    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNama_asli() {
        return nama_asli;
    }

    public void setNama_asli(String nama_asli) {
        this.nama_asli = nama_asli;
    }
}
