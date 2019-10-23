package com.allandroidprojects.payeat.testkuncoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.product.ItemDetailsActivity;
import com.allandroidprojects.payeat.scanQr;
import com.allandroidprojects.payeat.startup.MainActivity;
import com.allandroidprojects.payeat.utility.pin;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.allandroidprojects.payeat.testkuncoro.AdapterHistory;
import com.payeat.login.AppController;
import com.allandroidprojects.payeat.testkuncoro.DataHistory;
import com.payeat.login.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.allandroidprojects.payeat.R;

public class history_saldo extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{

    ListView list;
    SwipeRefreshLayout swipe;
    List<DataHistory> itemList = new ArrayList<DataHistory>();
    AdapterHistory adapter;
    int success;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_id, txt_nama, txt_alamat;
    String id, nama, alamat;
    public int treshold = 0;
    Button delete;
    public String saldo_saya;

    public String totalHarga;

    private static final String TAG = cart_kuncoro.class.getSimpleName();

    private static String url     = Server.URL + "hsaldo.php";

    public static final String TAG_TANGGAL     = "tanggal";
    public static final String TAG_DESKRIPSI   = "deskripsi";
    public static final String TAG_DEBIT       = "d";
    public static final String TAG_CREDIT      = "k";
    public static final String TAG_POSISI      = "posisi";


    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_saldo);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listhistory);
        adapter = new AdapterHistory(history_saldo.this, itemList);
        list.setAdapter(adapter);

/*
        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        */


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


    }

    public void onBackPressed() {

        Intent intent = new Intent(history_saldo.this, login.class);
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
        JsonArrayRequest jArr = new JsonArrayRequest(url +"?idu=" + getIntent().getStringExtra("idUser") , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        DataHistory item = new DataHistory();

                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                        item.setDebit(obj.getString(TAG_DEBIT));
                        item.setKredit(obj.getString(TAG_CREDIT));
                       /*
                        if(obj.getString(TAG_DEBIT) == "0"){


                              item.setJumlah("20000");
                          //  item.setJumlah(obj.getString(TAG_CREDIT));
                        } else if (obj.getString(TAG_CREDIT ) == "0"){
                            item.setJumlah("20000");
                           // item.setJumlah(obj.getString(TAG_DEBIT));
                        }*/

                        item.setPosisi(obj.getString(TAG_POSISI));



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



}
