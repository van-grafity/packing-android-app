package com.app.ivansuhendra.packinggla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.app.ivansuhendra.packinggla.databinding.ActivityNewPalletTransferBinding;
import com.app.ivansuhendra.packinggla.databinding.ActivityNewTransferNoteBinding;

public class NewTransferNoteActivity extends AppCompatActivity {
    private ActivityNewTransferNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTransferNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}