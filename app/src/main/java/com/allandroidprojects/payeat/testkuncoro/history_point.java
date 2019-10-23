package com.allandroidprojects.payeat.testkuncoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.login;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.payeat.login.AppController;
import com.payeat.login.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class history_point extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{


    ListView list;
    SwipeRefreshLayout swipe;
    List<DataHistoryPoint> itemList = new ArrayList<DataHistoryPoint>();
    AdapterHistoryPoint adapter;
    private static final String TAG = cart_kuncoro.class.getSimpleName();

    private static String url     = Server.URL + "hpoin.php";

    public static final String TAG_TANGGAL     = "tanggal";
    public static final String TAG_DESKRIPSI   = "deskripsi";
    public static final String TAG_DEBIT       = "d";
    public static final String TAG_CREDIT      = "k";
    public static final String TAG_POSISI      = "posisi";


    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_point);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listpoint);
        adapter = new AdapterHistoryPoint(history_point.this, itemList);
        list.setAdapter(adapter);

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

        Intent intent = new Intent(history_point.this, login.class);
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

                        DataHistoryPoint item = new DataHistoryPoint();

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
