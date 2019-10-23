package com.allandroidprojects.payeat.merchant;

/**
 * Created by thero on 4/23/2018.
 */

public class DataMerchant {

    private String id_merchant, nama_merchant, alamat_merchant, no_telp_merchant;

    public DataMerchant() {

    }

    public DataMerchant(String id_merchant, String nama_merchant, String alamat_merchant, String no_telp_merchant) {
        this.id_merchant = id_merchant;
        this.nama_merchant = nama_merchant;
        this.alamat_merchant = alamat_merchant;
        this.no_telp_merchant = no_telp_merchant;

    }

    public String getId_merchant() {
        return id_merchant;
    }

    public void setId_merchant(String id_merchant) {

        this.id_merchant = id_merchant;
    }

    public String getNama_merchant() {
        return nama_merchant;
    }

    public void setNama_merchant(String nama_merchant) {

        this.nama_merchant = nama_merchant;
    }

    public String getAlamat_merchant() {

        return alamat_merchant;
    }

    public void setAlamat_merchant(String alamat_merchant) {

        this.alamat_merchant = alamat_merchant;
    }

    public String getNo_telp_merchant() {

        return no_telp_merchant;
    }

    public void setNo_telp_merchant(String no_telp_merchant) {

        this.no_telp_merchant = no_telp_merchant;
    }


}
