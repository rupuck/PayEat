package com.allandroidprojects.payeat.testkuncoro;

/**
 * Created by thero on 4/9/2018.
 */

public class DataHistoryPoint {

    private String tanggal, deskripsi, jumlah, posisi, debit, kredit;

    public DataHistoryPoint() {
    }

    public DataHistoryPoint(String tanggal, String deskripsi, String jumlah, String posisi, String debit, String kredit) {
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.posisi = posisi;
        this.debit = debit;
        this.kredit = kredit;
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

    public String getJumlah(){
        return jumlah;
    }

    public void setJumlah(String jumlah){
        this.jumlah = jumlah;
    }

    public String getPosisi(){
        return posisi;
    }

    public void setPosisi(String posisi){
        this.posisi = posisi;
    }

    public String getDebit(){
        return debit;
    }

    public void setDebit(String debit){
        this.debit = debit;
    }

    public String getKredit(){
        return kredit;
    }

    public void setKredit(String kredit){
        this.kredit = kredit;
    }


}
