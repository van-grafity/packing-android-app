package com.app.ivansuhendra.packinggla.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.adapter.PalletLoadAdapter;
import com.app.ivansuhendra.packinggla.databinding.ActivityPalletLoadBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

import java.util.ArrayList;
import java.util.List;

public class PalletLoadActivity extends AppCompatActivity {
    private ActivityPalletLoadBinding binding;
    public static final int REQUEST_CODE = 123; // Any unique value you choose
    private ProgressDialog progressDialog;
    private PalletLoadAdapter mAdapter;
    private TransferViewModel transferViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalletLoadBinding.inflate(LayoutInflater.from(PalletLoadActivity.this));
        setContentView(binding.getRoot());

        progressDialog = GlobalVars.pgDialog(PalletLoadActivity.this);
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

        binding.rvTransferNote.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("scannedLoad")) {
            String scannedResult = intent.getStringExtra("scannedLoad");
            handleScannedResult(scannedResult);
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TransferNote> checkedItems = mAdapter.getCheckedItems();
                if (checkedItems.size() > 0) {
                    progressDialog.show(); // Show progress dialog before making the API call

                    // Extract transfer note ids from checked items
                    List<Integer> transferNoteIds = new ArrayList<>();
                    for (TransferNote note : checkedItems) {
                        transferNoteIds.add(note.getId());
                    }

                    // Convert list to array
                    Integer[] transferNoteIdsArray = transferNoteIds.toArray(new Integer[0]);

                    // Make the API call to post pallet load data
                    transferViewModel.postPalletLoadData(transferNoteIdsArray).observe(PalletLoadActivity.this, new Observer<APIResponse>() {
                        @Override
                        public void onChanged(APIResponse apiResponse) {
                            progressDialog.dismiss(); // Dismiss progress dialog after receiving response
                            // Handle API response here
                            Toast.makeText(PalletLoadActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(PalletLoadActivity.this, "No items selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleScannedResult(String scannedResult) {
        // Handle scanned result here
        Toast.makeText(PalletLoadActivity.this, scannedResult, Toast.LENGTH_SHORT).show();
        // Perform any additional actions based on the scanned result
        getData(scannedResult);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String scannedResult = data.getStringExtra("scannedLoad");
                Toast.makeText(PalletLoadActivity.this, scannedResult, Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle if the activity was canceled
            }
        }
    }

    private void getData(String palletNo) {
        transferViewModel.getPalletLoadData(palletNo).observe(this, apiResponse -> {

            binding.bgStatus.setBackgroundColor(Color.parseColor("#" + apiResponse.getData().getPalletTransfer().getColorCode()));
            binding.tvStatusProgressLayer.setText(apiResponse.getData().getPalletTransfer().getStatus());
            binding.tvPalletNo.setText("No. "+apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
            binding.tvRackLocation.setText(apiResponse.getData().getPalletTransfer().getRackNo());
            binding.tvLocationFrom.setText(apiResponse.getData().getPalletTransfer().getLocationFrom());
            binding.tvTotalCarton.setText(apiResponse.getData().getPalletTransfer().getTotalCarton());
            setDataAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());
        });
    }

    private void setDataAdapter(List<TransferNote> transferNotes) {
        mAdapter = new PalletLoadAdapter(this, transferNotes, "", new PalletLoadAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, TransferNote transferNote) {
                Toast.makeText(PalletLoadActivity.this, transferNote.getSerialNumber(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvTransferNote.setAdapter(mAdapter);
    }
}
