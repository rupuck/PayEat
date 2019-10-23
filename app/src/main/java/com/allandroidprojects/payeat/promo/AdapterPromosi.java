package com.allandroidprojects.payeat.promo;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.promo.DataPromosi;
import com.payeat.login.AppController;
import com.allandroidprojects.payeat.testkuncoro.Data;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterPromosi extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataPromosi> items;

    public int total_harga = 0;



    public AdapterPromosi(Activity activity, List<DataPromosi> items) {
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
            convertView = inflater.inflate(R.layout.listpromosi, null);

        TextView id = (TextView) convertView.findViewById(R.id.idPromosi);
        TextView nama = (TextView) convertView.findViewById(R.id.namaPromosi);
        TextView harga = (TextView) convertView.findViewById(R.id.hargaPromosi);
        TextView deskripsi = (TextView) convertView.findViewById(R.id.deskripsiPromosi);
        ImageView foto = (ImageView) convertView.findViewById(R.id.imagePromosi) ;
        // TextView foto = (TextView) convertView.findViewById(R.id.fotoKuncoro);
        DataPromosi data = items.get(position);
        nama.setText(data.getNama());
      //  id.setText(data.getId());
        harga.setText("Harga = "+data.getHarga());
        deskripsi.setText(data.getDeskripsi());

        //Picasso.get().load(data.getFoto()).into(foto);

        Picasso.get().load("http://payeat.store/admin/action/barang/image/gambar/"+data.getFoto()).into(foto);

        return convertView;
    }







}
