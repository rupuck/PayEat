package com.allandroidprojects.payeat.testkuncoro;

/**
 * Created by thero on 4/9/2018.
 */

public class DataNotification {

    private String tanggal, deskripsi;

    public DataNotification() {
    }

    public DataNotification(String tanggal, String deskripsi) {
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;

    }

    public String getTanggal(){
        return tanggal;
    }

    public void setTanggal(String tanggal){
        this.tanggal = tanggal;
    }

    public String getDeskripsi(){
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }




}
