package com.allandroidprojects.payeat.merchant;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class merchant extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{

    AlertDialog.Builder dialog;
    ListView list;
    SwipeRefreshLayout swipe;
    List<DataMerchant> itemList = new ArrayList<DataMerchant>();
    AdapterMerchant adapter;
    private static final String TAG = merchant.class.getSimpleName();

    private static String url     = Server.URL + "lihatmerchant.php";
    public static final String TAG_ID     = "id_merchant";
    public static final String TAG_NAMA     = "nama_merchant";
    public static final String TAG_ALAMAT   = "alamat_merchant";
    public static final String TAG_TELP       = "no_telp_merchant";




    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);


        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listmerchant);
        adapter = new AdapterMerchant(merchant.this, itemList);
        list.setAdapter(adapter);

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

        Intent intent = new Intent(merchant
                .this, login.class);
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
                        DataMerchant item = new DataMerchant();
                        item.setId_merchant(obj.getString(TAG_ID));
                        item.setNama_merchant(obj.getString(TAG_NAMA));
                        item.setAlamat_merchant(obj.getString(TAG_ALAMAT));
                        item.setNo_telp_merchant(obj.getString(TAG_TELP));


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
