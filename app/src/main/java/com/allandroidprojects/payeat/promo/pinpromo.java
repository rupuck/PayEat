package com.allandroidprojects.payeat.promo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.utility.pin;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.payeat.login.AppController;
import com.payeat.login.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pinpromo extends AppCompatActivity {

    EditText pin;
    int success;
    private static final String TAG = pinpromo.class.getSimpleName();
    private static String url_redeem     = Server.URL + "redeem.php";
    private String url = Server.URL + "pin.php";
    private static final String TAG_SUCCESS = "success";
    String tag_json_obj = "json_obj_req";

    ConnectivityManager conMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpromo);
    }

    public void onBackPressed() {

        Intent intent = new Intent(pinpromo.this, login.class);
        startActivity(intent);
        finish();
    }

    public void validate(View view){



        pin = (EditText) findViewById(R.id.txt_pinpromo);
        String pin_input = pin.getText().toString();
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (pin_input.trim().length() > 0 ) {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {

                checkpin(pin_input, getIntent().getStringExtra("idUser"),  getIntent().getStringExtra("idpromo"));
            } else {
                Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext() ,"Pin tidak boleh kosong", Toast.LENGTH_LONG).show();
        }


    }

    public void checkpin(final String pin, final String iduser, final String idpromo){

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Berhasil Dibeli!", jObj.toString());
                        //   Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Redeem Point Berhasil !", Toast.LENGTH_LONG).show();
                        // menyimpan login ke session
                        redeempoint(idpromo, iduser);
                        // Memanggil main activity
                        Intent intent = new Intent(pinpromo.this, login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Pin Anda Salah!", Toast.LENGTH_LONG).show();
                        //   Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pin", pin);
                params.put("idu", iduser);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }


    public void redeempoint(final String id_promosi,final String id_user){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_redeem, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               Log.d(TAG, "Response: " + response.toString());

                try {

                    Log.d(TAG, "Berhasil Point: ");

                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(pinpromo.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
