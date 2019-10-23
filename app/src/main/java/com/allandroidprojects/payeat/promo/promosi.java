package com.allandroidprojects.payeat.promo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.login;

import com.allandroidprojects.payeat.testkuncoro.cart_kuncoro;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.payeat.login.AppController;
import com.payeat.login.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.allandroidprojects.payeat.R;

public class promosi extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{

    AlertDialog.Builder dialog;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataPromosi> itemList = new ArrayList<DataPromosi>();
    AdapterPromosi adapter;
    private static final String TAG = promosi.class.getSimpleName();

    private static String url     = Server.URL + "lihatpromosi.php";
    private static String url_redeem     = Server.URL + "tukarpoin.php";

    public static final String TAG_ID     = "id_promo";
    public static final String TAG_NAMA     = "nama_promo";
    public static final String TAG_DESKRIPSI   = "deskripsi";
    public static final String TAG_HARGA       = "harga_poin";
    public static final String TAG_FOTO      = "foto";



    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promosi);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listpromosi);
        adapter = new AdapterPromosi(promosi.this, itemList);
        list.setAdapter(adapter);

        TextView pointSaya = (TextView) findViewById(R.id.myPoint);
        pointSaya.setText("Point Anda  =  " + getIntent().getStringExtra("poin"));
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {

                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();

                       }
                   }
        );

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String idx = itemList.get(position).getId();

                final String harga_point = itemList.get(position).getHarga();
                final String my_point = getIntent().getStringExtra("poin");
                if (Integer.parseInt(my_point) < Integer.parseInt(harga_point)){
                    Toast.makeText(promosi.this, "Maaf Point anda tidak mencukupi", Toast.LENGTH_LONG).show();
                } else{

                    final CharSequence[] dialogitem = {"Gunakan Promo"};
                    dialog = new AlertDialog.Builder(promosi.this);
                    dialog.setTitle("Apakah Anda ingin menggunakan promo?");
                    dialog.setCancelable(true);
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            switch (which) {
                                case 0:

                                    Intent intent = new Intent(promosi.this, pinpromo.class);
                                    intent.putExtra("idUser", getIntent().getStringExtra("idUser"));
                                    intent.putExtra("poin", getIntent().getStringExtra("poin"));
                                    intent.putExtra("idpromo", idx);
                                    startActivity(intent);

                                    break;
                            }
                        }
                    }).show();
                }



            }

        });

    }


    public void onBackPressed() {

        Intent intent = new Intent(promosi.this, login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataPromosi item = new DataPromosi();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setHarga(obj.getString(TAG_HARGA));
                        item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                        item.setFoto(obj.getString(TAG_FOTO));


                        // menambah item ke array
                        itemList.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);

    }

    public void redeempoint(final String id_promosi,final String id_user){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_redeem, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(promosi.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idp", id_promosi);
                params.put("idu", id_user);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}
