package com.allandroidprojects.payeat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.product.ItemDetailsActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.payeat.login.AppController;
import com.payeat.login.Server;


import com.allandroidprojects.payeat.startup.MainActivity;

public class cart extends AppCompatActivity {

    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    int success;
    SharedPreferences sharedpreferences;

    private String URL_PRODUCTS = Server.URL + "lihat_cart.php";
    private String url = Server.URL + "scan.php";
    private String url2 = Server.URL + "inputcart.php";
    public static final String TAG_NAMA_BARANG = "nama_barang";
    public static final String TAG_QTY = "qty";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_HARGA = "harga";

    private static final String TAG_SUCCESS = "success";

    public String hargabarang = "";
    //String idu = getIntent().getStringExtra("idUser");

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work


    //a list to store all the products
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setCartLayout();
        String idx = getIntent().getStringExtra("idBarang");
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerviewCart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
           // loadProducts();
            checkBarang("18");
        } else {
            Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
        }




    }

    private void checkBarang(final String id) {
        // final String[] saldo = {""};
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_PRODUCTS, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                // Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    success = 1;

                    // Check for error node in json
                    if (success == 1) {
                       // String nama = jObj.getString(TAG_NAMA_BARANG);
                        String deskripsi = jObj.getString(TAG_QTY);
                       // String foto = jObj.getString(TAG_FOTO);
                        String harga = jObj.getString(TAG_HARGA);
                        hargabarang = harga;

                        //Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();

                        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(cart.this);
                        SharedPreferences.Editor editor = mSettings.edit();


                        Locale localeID = new Locale("in", "ID");
                        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                        String currency = format.format(Float.parseFloat(harga));

                        TextView asd = findViewById(R.id.txt_nama_barang);
                        //asd.setText(nama);

                        TextView pon = findViewById(R.id.txt_qty);
                        pon.setText(deskripsi);

                        TextView namee = findViewById(R.id.txt_harga);
                        namee.setText(harga);

/*
                        WebView myWebView = (WebView) findViewById(R.id.web2);
                        //myWebView.loadUrl("http://payeat.store/admin/action/barang/image/gambar/" + foto);
                        String url = "http://payeat.store/admin/action/barang/image/gambar/" + foto;
                        String myHtml = "<html><body>" +
                                "<img text-align='center' height='150' src='" + url + "'>" +
                                "</body></html>";
                        myWebView.loadData(myHtml, "text/html", null);
                        myWebView.setVerticalScrollBarEnabled(false);
                        myWebView.setHorizontalScrollBarEnabled(false);

*/

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
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);



    }


    protected void setCartLayout(){
        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        if(MainActivity.notificationCountCart >0){
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
        }else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }


    private void loadProducts() {

        /*
        * Creating a String Request
        * The request type is GET defined by first parameter
        * The URL is defined in the second parameter
        * Then we have a Response Listener and a Error Listener
        * In response listener we will get the JSON response as a String
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new Product(
                                        product.getString("id_barang"),
                                        product.getString("nama_barang"),
                                        product.getString("qty"),
                                        product.getString("harga")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            ProductsAdapter adapter = new ProductsAdapter(cart.this, productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
