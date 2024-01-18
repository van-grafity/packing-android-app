package com.app.ivansuhendra.packinggla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.app.ivansuhendra.packinggla.databinding.ActivityPalletReceiveDetailBinding;

public class PalletReceiveDetailActivity extends AppCompatActivity {
    private ActivityPalletReceiveDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalletReceiveDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}