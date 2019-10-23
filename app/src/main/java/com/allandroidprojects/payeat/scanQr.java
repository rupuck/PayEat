package com.allandroidprojects.payeat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.payeat.notification.NotificationCountSetClass;
import com.allandroidprojects.payeat.options.CartListActivity;
import com.allandroidprojects.payeat.product.ItemDetailsActivity;
import com.allandroidprojects.payeat.startup.MainActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.allandroidprojects.payeat.utility.ImageUrlUtils;
import com.payeat.login.AppController;
import com.payeat.login.Server;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanQr extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public String id_user ;
    SharedPreferences sharedpreferences;
    private static final String TAG_SUCCESS = "success";
    public final static String TAG_ID = "id";
    String idx;


    public static final String my_shared_preferences = "my_shared_preferences";
    private ZXingScannerView mScannerView;
    int imagePosition;
    String stringImageUri;
    int jumlah = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        id_user  = getIntent().getStringExtra("idUser");



        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 1);

        }

        mScannerView = new ZXingScannerView(this);
       // mScannerView= (ZXingScannerView) findViewById(R.id.zxscan);
        setContentView(mScannerView);

            super.onCreate(savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
       mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(scanQr.this, login.class);
        startActivity(intent);
    }

    public int getCount(){
        return jumlah;
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
     //   AlertDialog.Builder builder = new AlertDialog.Builder(this);
      //  builder.setTitle("Scan Result");
       // builder.setMessage(rawResult.getText());
      //  AlertDialog alert1 = builder.create();
      //  alert1.show();

        mScannerView.resumeCameraPreview(this);
       /* ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        imageUrlUtils.addCartListImageUri("1");
        MainActivity.notificationCountCart++;
        MainActivity.hitungcart++;

        NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
        startActivity(new Intent(scanQr.this, CartListActivity.class));
        */
        Intent intent = new Intent(scanQr.this, ItemDetailsActivity.class);
        //getIntent().getStringExtra("idUser");
        intent.putExtra("idUser", id_user);
        intent.putExtra("idBarang", rawResult.getText());
        finish();
        startActivity(intent);


    }


}



