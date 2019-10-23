package com.allandroidprojects.payeat.product;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.cart;
import com.allandroidprojects.payeat.fragments.ImageListFragment;
import com.allandroidprojects.payeat.fragments.ViewPagerActivity;
import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.notification.NotificationCountSetClass;
import com.allandroidprojects.payeat.options.CartListActivity;
import com.allandroidprojects.payeat.scanQr;
import com.allandroidprojects.payeat.startup.MainActivity;
import com.allandroidprojects.payeat.testkuncoro.cart_kuncoro;
import com.allandroidprojects.payeat.utility.ImageUrlUtils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.payeat.login.AppController;
import com.payeat.login.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.allandroidprojects.payeat.login.my_shared_preferences;
//import static com.allandroidprojects.payeat.startup.MainActivity.TAG_ID;

public class ItemDetailsActivity extends AppCompatActivity {
    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    int success;
    SharedPreferences sharedpreferences;


    private String url = Server.URL + "scan.php";
    private String url2 = Server.URL + "inputcart.php";
    public static final String TAG_NAMA_BARANG = "nama";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_FOTO = "foto";
    public static final String TAG_HARGA = "harga";
    private static final String TAG_SUCCESS = "success";

    public String hargabarang = "";



    int imagePosition;
    String stringImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
       MainActivity asd = new MainActivity();
     //   String idx = sharedpreferences.getString(asd.TAG_ID, null);
//
    //    String idx = asd.getGlobal_id();

       String idx = getIntent().getStringExtra("idBarang");



        //Getting image uri from previous screen
     /*
        if (getIntent() != null) {
            stringImageUri = getIntent().getStringExtra(ImageListFragment.STRING_IMAGE_URI);
            imagePosition = getIntent().getIntExtra(ImageListFragment.STRING_IMAGE_URI,0);
        }
        Uri uri = Uri.parse(stringImageUri);
        mImageView.setImageURI(uri);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ItemDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("position", imagePosition);
                    startActivity(intent);

            }
        });

        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(stringImageUri);
                Toast.makeText(ItemDetailsActivity.this,"Item added to cart.",Toast.LENGTH_SHORT).show();
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
            }
        });

        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(stringImageUri);
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));

            }
        });
        */






        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            checkBarang(idx);
        } else {
            Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ItemDetailsActivity.this, login.class);
        startActivity(intent);
    }

    private void checkBarang(final String id) {
        // final String[] saldo = {""};
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {
                // Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    success = 1;

                    // Check for error node in json
                    if (success == 1) {
                        String nama = jObj.getString(TAG_NAMA_BARANG);
                        String deskripsi = jObj.getString(TAG_DESKRIPSI);
                        String foto = jObj.getString(TAG_FOTO);
                        String harga = jObj.getString(TAG_HARGA);
                        hargabarang = harga;

                        //Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_LONG).show();

                        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(ItemDetailsActivity.this);
                        SharedPreferences.Editor editor = mSettings.edit();


                        Locale localeID = new Locale("in", "ID");
                        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
                        String currency = format.format(Float.parseFloat(harga));

                        TextView asd = findViewById(R.id.txt_hargaBarang);
                        asd.setText(currency);

                        TextView pon = findViewById(R.id.txt_namaBarang);
                        pon.setText(nama);

                        TextView namee = findViewById(R.id.txt_deskripsi);
                        namee.setText(deskripsi);


                        WebView myWebView = (WebView) findViewById(R.id.imagodei);
                        //myWebView.loadUrl("http://payeat.store/admin/action/barang/image/gambar/" + foto);
                        String url = "http://payeat.store/admin/action/barang/image/gambar/" + foto;
                        String myHtml = "<html><body>" +
                                "<img text-align='center' height='150' src='" + url + "'>" +
                                "</body></html>";
                        myWebView.loadData(myHtml, "text/html", null);
                        myWebView.setVerticalScrollBarEnabled(false);
                        myWebView.setHorizontalScrollBarEnabled(false);



                    } else {
                       /* Toast.makeText(getApplicationContext(),
                                jObj.getString("ERROR"), Toast.LENGTH_LONG).show();*/
                        Intent intent = new Intent(ItemDetailsActivity.this, login.class);
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "QR Code Tidak Terdaftar", Toast.LENGTH_LONG).show();
                    }




                } catch (JSONException e) {
                    // JSON error

                    Intent intent = new Intent(ItemDetailsActivity.this, login.class);
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "QR Code Tidak Terdaftar", Toast.LENGTH_LONG).show();

                  /*  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();*/
                }

            }
        }

                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(ItemDetailsActivity.this, login.class);
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "QR Code Tidak Terdaftar", Toast.LENGTH_LONG).show();
              /*  Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
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


    public void addCart(View view){

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

                TextView asd = findViewById(R.id.txt_quantity);
                String quantity = asd.getText().toString();
                String idx = getIntent().getStringExtra("idBarang");
                String idu = getIntent().getStringExtra("idUser");


                kirimData(idu, idx, quantity, hargabarang);
               // MainActivity.notificationCountCart++;
               // NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
              //  ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
              //  imageUrlUtils.addCartListImageUri("1");

        } else {
            Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Barang Berhasil dimasukkan ke dalam cart");
        builder.setMessage("Apakah anda ingin melanjutkan berbelanja ?");
        builder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ItemDetailsActivity.this, login.class);
                        finish();
                        startActivity(intent);

                    }
                });
        builder.setNegativeButton("Check Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ItemDetailsActivity.this, cart_kuncoro.class);
                intent.putExtra("idUser", getIntent().getStringExtra("idUser"));
                MainActivity.notificationCountCart++;
                finish();
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();




    }

    public void kirimData(final String idu, final String idb, final String jumlah, final String harga){



        StringRequest strReq = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("idu", idu);
                params.put("idb", idb);
                params.put("jumlah", jumlah);
                params.put("harga", harga);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Barang Berhasil dimasukkan ke dalam cart ! " );
        AlertDialog alert1 = builder.create();
        alert1.show();


    }



    public void plus(View view){
        TextView asd = findViewById(R.id.txt_quantity);
        String zxc = asd.getText().toString();

        int counts = Integer.parseInt(zxc);
        counts++;

        asd.setText(Integer.toString(counts));


    }

    public void minus(View view){
        TextView asd = findViewById(R.id.txt_quantity);

        String zxc = asd.getText().toString();

        int counts = Integer.parseInt(zxc);
        if(counts >1)counts--;
        asd.setText(Integer.toString(counts));
    }
}
