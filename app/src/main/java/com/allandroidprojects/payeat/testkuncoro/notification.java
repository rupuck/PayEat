package com.allandroidprojects.payeat.testkuncoro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.startup.MainActivity;
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
import com.allandroidprojects.payeat.R;

public class notification extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView list;
    SwipeRefreshLayout swipe;
    List<DataNotification> itemList = new ArrayList<DataNotification>();
    AdapterNotification adapter;
    private static final String TAG = notification.class.getSimpleName();

    private static String url     = Server.URL + "notif.php";

    public static final String TAG_TANGGAL     = "tanggal";
    public static final String TAG_DESKRIPSI   = "isi";


    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.notificationCount > 0){
            setContentView(R.layout.activity_notifkosong);
        }
        else{

            setContentView(R.layout.activity_notification);
            swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            list    = (ListView) findViewById(R.id.listnotif);
            adapter = new AdapterNotification(notification.this, itemList);
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


    }

    public void onBackPressed() {

        Intent intent = new Intent(notification.this, login.class);
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

                        DataNotification item = new DataNotification();

                        item.setTanggal(obj.getString(TAG_TANGGAL));
                        item.setDeskripsi(obj.getString(TAG_DESKRIPSI));

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
