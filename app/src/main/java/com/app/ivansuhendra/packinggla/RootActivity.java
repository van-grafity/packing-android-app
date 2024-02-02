package com.app.ivansuhendra.packinggla;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.ivansuhendra.packinggla.ui.activity.SplashActivity;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(RootActivity.this, SplashActivity.class));
        finish();
    }
}
