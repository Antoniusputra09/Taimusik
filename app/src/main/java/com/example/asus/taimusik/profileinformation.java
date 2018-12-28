package com.example.asus.taimusik;

import com.google.firebase.storage.StorageReference;

public class profileinformation {

    public String kota;
    public String tanggal;
    public String nama_asli;
    public String nama_user;
    public String email;
    public String password2;
    public String kelamin;
    public String fotopp;
    //public String jenis_kelamin;



    public profileinformation(String Nama, String User,String kota, String tanggal){
        this.kota=kota;
        this.tanggal=tanggal;

    }

    public profileinformation(String foto, String Nama, String User, String Tanggal, String Kota) {
        this.fotopp = foto;
        this.nama_asli = Nama;
        this.nama_user = User;
        this.tanggal = Tanggal;
        this.kota = Kota;
    }
}
