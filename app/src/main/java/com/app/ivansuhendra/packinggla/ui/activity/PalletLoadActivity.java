package com.app.ivansuhendra.packinggla.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
                            getData(binding.tvPalletNo.getText().toString().trim());
                        }
                    });
                } else {
                    Toast.makeText(PalletLoadActivity.this, "No items selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleScannedResult(String scannedResult) {
        // Perform any additional actions based on the scanned result
        getData(scannedResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String scannedResult = data.getStringExtra("scannedLoad");
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
            if (apiResponse.getStatus().equals("error")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PalletLoadActivity.this);
                alertDialogBuilder
                        .setMessage(apiResponse.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }else {
                binding.bgStatus.setBackgroundColor(Color.parseColor("#" + apiResponse.getData().getPalletTransfer().getColorCode()));
                binding.tvStatusProgressLayer.setText(apiResponse.getData().getPalletTransfer().getStatus());
                binding.tvPalletNo.setText(apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
                binding.tvRackLocation.setText(apiResponse.getData().getPalletTransfer().getRackNo());
                binding.tvLocationFrom.setText(apiResponse.getData().getPalletTransfer().getLocationFrom());
                binding.tvTotalCarton.setText(apiResponse.getData().getPalletTransfer().getTotalCarton());

                if (!allTransferNotesWithoutCartons(apiResponse.getData().getPalletTransfer().getTransferNotes())) {
                    setDataAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PalletLoadActivity.this);
                    alertDialogBuilder
                            .setMessage("Carton is Empty!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
//                for (TransferNote transferNote : apiResponse.getData().getPalletTransfer().getTransferNotes()) {
//                    if (transferNote.getCarton() == null || transferNote.getCarton().isEmpty()) {
//                        // Handle case when cartons for this transfer note are null or empty
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PalletLoadActivity.this);
//                        alertDialogBuilder
//                                .setMessage("Pallet is Empty")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        finish();
//                                    }
//                                });
//
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.cancel();
//                        }
//                    } else {
//                        // Handle case when cartons for this transfer note are not empty
//                        setDataAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());
//                        if (progressDialog != null && progressDialog.isShowing()) {
//                            progressDialog.cancel();
//                        }
//                    }
//                }
            }
        });
    }

    public boolean allTransferNotesWithoutCartons(ArrayList<TransferNote> transferNotes) {
        for (TransferNote note : transferNotes) {
            if (note.getCarton() != null && !note.getCarton().isEmpty()) {
                return false; // Jika salah satu transfer note memiliki cartons, kembalikan false
            }
        }
        return true; // Jika tidak ada transfer note yang memiliki cartons, kembalikan true
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
