package com.allandroidprojects.payeat.kontak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.allandroidprojects.payeat.R;
import com.allandroidprojects.payeat.login;
import com.allandroidprojects.payeat.product.ItemDetailsActivity;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(about.this, login.class);
        startActivity(intent);
    }

}
