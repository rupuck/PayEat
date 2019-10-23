package com.allandroidprojects.payeat.promo;

/**
 * Created by thero on 3/26/2018.
 */

public class DataPromosi {

    private String id, foto, nama, deskripsi, harga;

    public DataPromosi() {
    }

    public DataPromosi(String id, String foto, String nama, String deskripsi, String harga) {
        this.id = id;
        this.foto = foto;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;


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

    public String getFoto() {

        return foto;
    }

    public void setFoto(String foto) {

        this.foto = foto;
    }

    public String getDeskripsi() {

        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {

        this.deskripsi = deskripsi;
    }

    public String getHarga() {

        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }


}
