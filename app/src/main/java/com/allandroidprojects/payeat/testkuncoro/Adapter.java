package com.allandroidprojects.payeat.testkuncoro;

/**
 * Created by thero on 3/26/2018.
 */


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.allandroidprojects.payeat.R;
import com.payeat.login.AppController;
import com.allandroidprojects.payeat.testkuncoro.Data;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public int total_harga = 0;


    public Adapter(Activity activity, List<Data> items) {
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
            convertView = inflater.inflate(R.layout.listkuncoro, null);

        TextView id = (TextView) convertView.findViewById(R.id.idKuncoro);
        TextView nama = (TextView) convertView.findViewById(R.id.namaKuncoro);
        TextView harga = (TextView) convertView.findViewById(R.id.hargaKuncoro);
        TextView qty = (TextView) convertView.findViewById(R.id.kuantitasKuncoro);

        //
       // TextView foto = (TextView) convertView.findViewById(R.id.fotoKuncoro);

        Data data = items.get(position);

        setTotal_harga(Integer.parseInt(data.getHarga())*Integer.parseInt(data.getQty()));

        nama.setText(data.getNama());
        id.setText(data.getId());

        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        String currency = format.format(Float.parseFloat(data.getHarga()));


        harga.setText(currency);
        qty.setText("x   " +data.getQty());


      //  foto.setText(data.getFoto());

        WebView myWebView = (WebView) convertView.findViewById(R.id.fotoKuncor);
        String url = "http://payeat.store/admin/action/barang/image/gambar/" + data.getFoto();
        String myHtml = "<html><body>" +
                "<img text-align='center' height='50' src='" + url + "'>" +
                "</body></html>";
        myWebView.loadData(myHtml, "text/html", null);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);




        return convertView;
    }



    public void setTotal_harga(int total_harga){
        this.total_harga += total_harga;
    }

    public int getTotal_harga(){
        return total_harga;
    }

    public int getTotal_Harga(int x){
        return 1;
    }



}
