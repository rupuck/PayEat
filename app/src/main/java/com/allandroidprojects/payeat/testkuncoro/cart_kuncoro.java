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
import com.allandroidprojects.payeat.promo.pinpromo;
import com.allandroidprojects.payeat.promo.promosi;
import com.allandroidprojects.payeat.scanQr;
import com.allandroidprojects.payeat.startup.MainActivity;
import com.allandroidprojects.payeat.utility.pin;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.allandroidprojects.payeat.testkuncoro.Adapter;
import com.payeat.login.AppController;
import com.allandroidprojects.payeat.testkuncoro.Data;
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

import com.allandroidprojects.payeat.startup.MainActivity;


import com.allandroidprojects.payeat.R;

public class cart_kuncoro extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener{

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
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



    private static String url_select     = Server.URL + "lihatcart.php";
    private static String url_saldo     = Server.URL + "rekening.php";
    private static String url_delete     = Server.URL + "deletecart.php";

    public static final String TAG_NAMA_BARANG     = "nama_barang";
    public static final String TAG_HARGA           = "harga";
    public static final String TAG_QTY             = "qty";
    public static final String TAG_FOTO            = "foto";
    public static final String TAG_SALDO            = "saldo";
    public static final String TAG_POIN            = "poin";
    public static final String TAG_TOTAL            = "total";



    public static final String TAG_ALAMAT   = "alamat";
    public static final String TAG_ID              = "id_barang";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.notificationCountCart <= 0){
            setContentView(R.layout.activity_cart);
        } else {
            setContentView(R.layout.activity_cart_kuncoro);


       // String idu = getIntent().getStringExtra("idUser");

      //  setSupportActionBar(toolbar);

        // menghubungkan variablel pada layout dan pada java
      //  fab     = (FloatingActionButton) findViewById(R.id.fab_add);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.listed);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(cart_kuncoro.this, itemList);
        list.setAdapter(adapter);
        kirimData(getIntent().getStringExtra("idUser"));
       // kirimData("18");

        kirimDataSaldo(getIntent().getStringExtra("idUser"));


        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {

                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           kirimData(getIntent().getStringExtra("idUser"));
                           //kirimData("18");
                           callVolley();



                           Locale localeID = new Locale("in", "ID");
                           NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
//                           String currency = format.format(Float.parseFloat(getTotalHargas()));
                           TextView total = (TextView) findViewById(R.id.total_harga);
                         //  total.setText(currency);
                       }
                   }
        );

     // listview ditekan lama akan menampilkan dua pilihan edit atau delete data
     /*   list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemList.get(position).getId();
                final CharSequence[] dialogitem = {"Delete"};
                dialog = new AlertDialog.Builder(cart_kuncoro.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                delete_cart(idx, getIntent().getStringExtra("idUser"));
                                kirimData(getIntent().getStringExtra("idUser"));
                                callVolley();
                                kirimDataSaldo(getIntent().getStringExtra("idUser"));
                                break;
                        }
                    }
                }).show();
                return false;
            }


        });
        */

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String idx = itemList.get(position).getId();
                    final CharSequence[] dialogitem = {"Delete"};
                    dialog = new AlertDialog.Builder(cart_kuncoro.this);
                    dialog.setCancelable(true);
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            switch (which) {
                                case 0:
                                    delete_cart(idx, getIntent().getStringExtra("idUser"));
                                    kirimData(getIntent().getStringExtra("idUser"));
                                    callVolley();
                                    kirimDataSaldo(getIntent().getStringExtra("idUser"));
                                    break;
                            }
                        }
                    }).show();


                }

            });


        delete = (Button) findViewById(R.id.btn_hapus);
        View.OnClickListener myButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);

            }
        };
//        delete.setOnClickListener(myButtonClickListener);

        }

    }
    public void onBackPressed() {

        Intent intent = new Intent(cart_kuncoro.this, login.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

        if(MainActivity.notificationCountCart <= 0) {
            setContentView(R.layout.activity_cart);
        }
        else{
            itemList.clear();
            adapter.notifyDataSetChanged();
            kirimData(getIntent().getStringExtra("idUser"));
            //kirimData("18");
            callVolley();
            kirimDataSaldo(getIntent().getStringExtra("idUser"));
        }


    }
    // untuk mengosongi edittext pada form
    private void kosong(){
        txt_id.setText(null);
        txt_nama.setText(null);
        txt_alamat.setText(null);
    }

    public void belanja(View view){
        Intent intent = new Intent(cart_kuncoro.this, login.class);
        startActivity(intent);
    }

    // untuk menampilkan dialog from biodata
    private void DialogForm(String idx, String namax, String alamat, String button) {/*
        dialog = new AlertDialog.Builder(cart_kuncoro.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.formkuncoro, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form Biodata");

        txt_id      = (EditText) dialogView.findViewById(R.id.id);
        txt_nama    = (EditText) dialogView.findViewById(R.id.namaKuncoro);
        txt_alamat  = (EditText) dialogView.findViewById(R.id.hargaKuncoro);

        if (!idx.isEmpty()){
            txt_id.setText(idx);
            txt_nama.setText(nama);
            txt_alamat.setText(alamat);
        } else {
            kosong();
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id      = txt_id.getText().toString();
                nama    = txt_nama.getText().toString();
                alamat  = txt_alamat.getText().toString();

                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
            }
        });

        dialog.show();
        */
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select +"?idu=" + getIntent().getStringExtra("idUser") , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA_BARANG));
                        item.setHarga(obj.getString(TAG_HARGA));
                        item.setFoto(obj.getString(TAG_FOTO));
                        item.setQty(obj.getString(TAG_QTY));



                        // menambah item ke array
                        itemList.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                //Toast.makeText(getApplicationContext(), getTotalHargas(), Toast.LENGTH_LONG).show();



               //





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

        TextView total = (TextView) findViewById(R.id.total_harga);
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
      //  setTotalHarga(Integer.toString(adapter.getTotal_harga()));
       // total.setText(format.format(Float.parseFloat(Integer.toString(adapter.getTotal_harga()))));
    }

    private void callVolley2(final String idu){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
      //  JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>(){
            @Override
          //  public void onResponse(JSONArray response) {
              public void onResponse(String response) {
                Log.d(TAG, response.toString());

                // Parsing json
             //   for (int i = 0; i < response.length(); i++) {

                for (int i = 0; i < response.length(); i++) {
                    try {

                        //JSONObject obj = response.getJSONObject(i);
                        JSONObject obj = new JSONObject(response);
                        Data item = new Data();

                        //item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA_BARANG));
                        item.setHarga(obj.getString(TAG_HARGA));
                        item.setFoto(obj.getString(TAG_FOTO));
                        item.setQty(obj.getString(TAG_QTY));


                        // menambah item ke array
                        itemList.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }




                TextView total = (TextView) findViewById(R.id.total_harga);
                Locale localeID = new Locale("in", "ID");
                NumberFormat format = NumberFormat.getCurrencyInstance(localeID);


                total.setText(format.format(Float.parseFloat(Integer.toString(adapter.getTotal_harga()))));



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
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idu", idu);
                return params;
            }

        };

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void kirimData(final String idu){



        StringRequest strReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "yg response " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("idu", getIntent().getStringExtra("idUser"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void kirimDataSaldo(final String id) {
        // final String[] saldo = {""};
        StringRequest strReq = new StringRequest(Request.Method.POST, url_saldo, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                // Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    success = 1;

                    // Check for error node in json
                    if (success == 1) {
                        String saldo = jObj.getString(TAG_SALDO);
                        String poin = jObj.getString(TAG_POIN);
                        String total_harga = jObj.getString(TAG_TOTAL);

                        Locale localeID = new Locale("in", "ID");
                        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                        String currency = format.format(Float.parseFloat(saldo));

                        //Toast.makeText(getApplicationContext(), currency, Toast.LENGTH_LONG).show();

                        TextView asd = findViewById(R.id.total_saldo);
                        asd.setText(currency);

                        TextView total = (TextView) findViewById(R.id.total_harga);
                        //setTotalHarga(Integer.toString(adapter.getTotal_harga()));
                        total_harga.replaceAll("\\s+","");
                        if(total_harga != null || total_harga !=""){
                            setTotalHarga(total_harga);
                            setSaldo_saya(saldo);
                            String currencys = format.format(Float.parseFloat(total_harga));
                            total.setText(currencys);

                        }else {

                            setTotalHarga("0");
                            setSaldo_saya(saldo);
                            String currencys = format.format(Float.parseFloat("0"));
                            total.setText(currencys);

                           // Toast.makeText(getApplicationContext(), total_harga, Toast.LENGTH_LONG).show();
                        }




                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString("ERROR"), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idu",  getIntent().getStringExtra("idUser"));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);



    }

    public void checksaldo(View view){

        int a = Integer.parseInt(getSaldo_saya());
        int b = Integer.parseInt(getTotalHargas());

        if(a>=b){
            if(b==0){
                Toast.makeText(cart_kuncoro.this, "KERANJANG KOSONG", Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(cart_kuncoro.this, pin.class);
                intent.putExtra("idUser", getIntent().getStringExtra("idUser"));
                startActivity(intent);
            }


        }else  Toast.makeText(cart_kuncoro.this, "SALDO TIDAK MENCUKUPI", Toast.LENGTH_LONG).show();




    }

    // fungsi untuk menyimpan atau update
    private void simpan_update() {
        /*
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id.isEmpty()){
            url = url_insert;
        } else {
            url = url_update;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        callVolley();
                        kosong();

                        Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(cart_kuncoro.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id.isEmpty()){
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                } else {
                    params.put("id", id);
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
  */  }

    // fungsi untuk get edit data
    private void edit(final String idx){
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_ID);
                        String namax    = jObj.getString(TAG_NAMA);
                        String alamatx  = jObj.getString(TAG_ALAMAT);

                        DialogForm(idx, namax, alamatx, "UPDATE");

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(cart_kuncoro.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        */
    }

    // fungsi untuk menghapus
    public void delete_cart(final String id_barang,final String id_user){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();
                        onRefresh();
                      //  Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                       // Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(cart_kuncoro.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idb", id_barang);
                params.put("idu", id_user);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void delete(final String idx){
        /*
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        callVolley();

                        Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(cart_kuncoro.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(cart_kuncoro.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
   */ }

   public void setTotalHarga(String totalHarga){
        this.totalHarga = totalHarga;
   }
    public String getTotalHargas(){
        return totalHarga;
    }

    public void setSaldo_saya(String saldo_saya){
        this.saldo_saya = saldo_saya;
    }

    public String getSaldo_saya(){
        return saldo_saya;
    }

    }

