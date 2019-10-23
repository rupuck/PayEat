package com.allandroidprojects.payeat.merchant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.promo.DataPromosi;
import com.allandroidprojects.payeat.testkuncoro.Data;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by thero on 4/23/2018.
 */

public class AdapterMerchant extends BaseAdapter {


    private Activity activity;
    private LayoutInflater inflater;
    private List<DataMerchant> items;

    public AdapterMerchant(Activity activity, List<DataMerchant> items) {
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

    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listmerchant, null);

        TextView id = (TextView) convertView.findViewById(R.id.txt_id_merchant);
        TextView nama = (TextView) convertView.findViewById(R.id.txt_nama_merchant);
        TextView harga = (TextView) convertView.findViewById(R.id.txt_alamat_merchant);
        TextView deskripsi = (TextView) convertView.findViewById(R.id.txt_no_telp_merchant);

        // TextView foto = (TextView) convertView.findViewById(R.id.fotoKuncoro);
        DataMerchant data = items.get(position);

        nama.setText(data.getNama_merchant());
      //  id.setText(data.getId_merchant());
        harga.setText(data.getAlamat_merchant());
        deskripsi.setText(data.getNo_telp_merchant());
        return convertView;
    }




}
