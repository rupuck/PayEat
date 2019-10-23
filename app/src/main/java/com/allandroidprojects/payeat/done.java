package com.allandroidprojects.payeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.allandroidprojects.payeat.startup.MainActivity;

public class done extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
    }

    public void yayHome(View view){
        Intent i = new Intent(done.this, MainActivity.class);
        startActivity(i);
    }
}
