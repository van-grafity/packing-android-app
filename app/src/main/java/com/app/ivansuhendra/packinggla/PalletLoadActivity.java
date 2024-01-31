package com.app.ivansuhendra.packinggla;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.app.ivansuhendra.packinggla.databinding.ActivityPalletLoadBinding;

public class PalletLoadActivity extends AppCompatActivity {
    private ActivityPalletLoadBinding binding;
    static final int REQUEST_CODE = 123; // Any unique value you choose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalletLoadBinding.inflate(LayoutInflater.from(PalletLoadActivity.this));
        setContentView(binding.getRoot());




    }
}