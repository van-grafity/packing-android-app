package com.app.ivansuhendra.packinggla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.app.ivansuhendra.packinggla.databinding.ActivityPalletLoadBinding;

public class PalletLoadActivity extends AppCompatActivity {
    private ActivityPalletLoadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalletLoadBinding.inflate(LayoutInflater.from(PalletLoadActivity.this));
        setContentView(binding.getRoot());


    }
}