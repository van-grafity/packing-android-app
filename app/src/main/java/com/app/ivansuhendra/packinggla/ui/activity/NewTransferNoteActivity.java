package com.app.ivansuhendra.packinggla.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.app.ivansuhendra.packinggla.databinding.ActivityNewPalletTransferBinding;
import com.app.ivansuhendra.packinggla.databinding.ActivityNewTransferNoteBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

public class NewTransferNoteActivity extends AppCompatActivity {
    private ActivityNewTransferNoteBinding binding;
    private TransferViewModel transferViewModel;
    private PalletTransfer mPallet;
    private ProgressDialog progressDialog;

    private static final int REQUEST_SCAN_QR = 1; // Unique request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTransferNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initializePalletTransferDetails();

        binding.btnAddCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTransferNoteActivity.this, ScanQrActivity.class);
                startActivityForResult(intent, REQUEST_SCAN_QR);
            }
        });
    }

    private void initializePalletTransferDetails() {
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        progressDialog = GlobalVars.pgDialog(NewTransferNoteActivity.this);
        mPallet = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);

        binding.tvTransactionNumber.setText("No.-");
        binding.tvPalletNo.setText(mPallet.getPalletSerialNumber());
        binding.tvTotalCarton.setText(mPallet.getTotalCarton());
        binding.tvLocationFrom.setText(mPallet.getLocationFrom());

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }

        binding.rvCarton.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_QR && resultCode == RESULT_OK) {
            String scannedResult = data.getStringExtra("scannedResult");
            // Process the scannedResult as needed in EditTransferNoteActivity
            progressDialog.show(); // Show loading indicator
            transferViewModel.searchCartonLiveData(scannedResult).observe(NewTransferNoteActivity.this, new Observer<APIResponse>() {
                @Override
                public void onChanged(APIResponse apiResponse) {
                    progressDialog.show(); // Show loading indicator
                    if (apiResponse.getStatus().equals("success")) {
                        Toast.makeText(NewTransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // insert ke database dan update adapter
//                        mDbHelper.insertCarton(apiResponse.getData().getCarton());
//                        mAdapter.notifyDataSetChanged();
//                        provideDbDataFromServer();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewTransferNoteActivity.this);
                        alertDialogBuilder
                                .setMessage(apiResponse.getMessage())
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    }
                }
            });
        }
    }
}