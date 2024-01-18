package com.app.ivansuhendra.packinggla;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.databinding.ActivityEditTransferNoteBinding;
import com.app.ivansuhendra.packinggla.databinding.ActivityPalletTransferDetailBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

public class EditTransferNoteActivity extends AppCompatActivity {
    private ActivityEditTransferNoteBinding binding;
    private TransferNote transferNote;
    private PalletTransfer palletTransfer;
    private TransferViewModel transferViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTransferNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        transferNote = getIntent().getParcelableExtra(GlobalVars.TRANSFER_NOTE_LIST);
        palletTransfer = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);

        binding.tvTransferNoteSerial.setText(transferNote.getSerialNumber());
        binding.tvFrom.setText(palletTransfer.getLocationFrom());
        binding.tvPalletNo.setText(palletTransfer.getPalletSerialNumber());
        binding.tvTotalCarton.setText(palletTransfer.getTotalCarton());

        initializeData();

        binding.btnAddCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTransferNoteActivity.this, ScanQrActivity.class));
            }
        });

        binding.btnEditTransferNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void initializeData() {
        binding.rvCarton.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        transferViewModel.getPalletTransferLiveData(palletTransfer.getId()).observe(EditTransferNoteActivity.this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                Toast.makeText(EditTransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}