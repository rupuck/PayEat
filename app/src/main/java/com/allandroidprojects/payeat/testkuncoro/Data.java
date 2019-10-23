package com.allandroidprojects.payeat.testkuncoro;

/**
 * Created by thero on 3/26/2018.
 */

public class Data {

    private String id, nama, alamat, harga, qty, foto;

    public Data() {
    }

    public Data(String id, String nama, String alamat, String harga, String foto, String qty) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.harga = harga;
        this.foto = foto;
        this.qty = qty;

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

    public String getQty() {

        return qty;
    }

    public void setQty(String qty) {

        this.qty = qty;
    }

    public String getHarga() {

        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}
