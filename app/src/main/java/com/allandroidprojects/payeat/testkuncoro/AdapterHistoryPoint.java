package com.allandroidprojects.payeat.testkuncoro;

/**
 * Created by thero on 4/9/2018.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allandroidprojects.payeat.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterHistoryPoint extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataHistoryPoint> items;

    //public int total_harga = 0;


    public AdapterHistoryPoint(Activity activity, List<DataHistoryPoint> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listpoint, null);

        TextView tanggal = (TextView) convertView.findViewById(R.id.txt_tanggalpoint);
        TextView deskripsi = (TextView) convertView.findViewById(R.id.txt_deskripsipoint);
        TextView jumlah = (TextView) convertView.findViewById(R.id.txt_jumlahpoint);
        TextView posisi = (TextView) convertView.findViewById(R.id.txt_posisipoint);

        //
        // TextView foto = (TextView) convertView.findViewById(R.id.fotoKuncoro);

        DataHistoryPoint data = items.get(position);

     //   setTotal_harga(Integer.parseInt(data.getHarga())*Integer.parseInt(data.getQty()));

        tanggal.setText(data.getTanggal());
        deskripsi.setText(data.getDeskripsi());
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);



        String currency2 = format.format(Float.parseFloat(data.getPosisi()));
        posisi.setText(currency2);

        if(Integer.parseInt(data.getDebit()) == 0){
            String currency = format.format(Float.parseFloat(data.getKredit()));
            jumlah.setText("+ " + currency);
            jumlah.setTextColor(Color.GREEN);
        }

        if(Integer.parseInt(data.getKredit()) == 0){
            String currency = format.format(Float.parseFloat(data.getDebit()));
            jumlah.setText("- "+currency);
            jumlah.setTextColor(Color.RED);
        }



        return convertView;
    }







}
