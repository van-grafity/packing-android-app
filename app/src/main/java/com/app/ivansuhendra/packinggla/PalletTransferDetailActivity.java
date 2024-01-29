package com.app.ivansuhendra.packinggla;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.ivansuhendra.packinggla.adapter.TransferNoteAdapter;
import com.app.ivansuhendra.packinggla.databinding.ActivityPalletTransferDetailBinding;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.ui.ViewState;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

import java.util.List;

public class PalletTransferDetailActivity extends AppCompatActivity {

    private static final String TAG = "PalletTransferDetail";

    private ActivityPalletTransferDetailBinding binding;
    private PalletTransfer mPallet;
    private TransferViewModel transferViewModel;
    private TransferNoteAdapter mAdapter;
    private ProgressDialog progressDialog;

    private enum IntentType {
        EDIT_TRANSFER_NOTE,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeClickListeners();
        initializePalletTransferDetails();
        initializeTransferNoteRecyclerView();
    }

    private void initializeViews() {
        binding = ActivityPalletTransferDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.rvTransferNote.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Panggil method untuk melakukan refresh data
                refreshData();
            }
        });
    }

    private void refreshData() {
        // Method ini akan dipanggil saat pengguna menarik SwipeRefreshLayout
        // Taruh kode untuk refresh data di sini, misalnya:
        initializeTransferNoteRecyclerView(); // Refresh RecyclerView

        // Setelah selesai refresh, beritahu SwipeRefreshLayout untuk menghentikan animasi refresh
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void initializeClickListeners() {
        binding.btnItemTransferNote.setOnClickListener(view -> startTransferNoteActivity(IntentType.EDIT_TRANSFER_NOTE));
        binding.btnNewTransferNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PalletTransferDetailActivity.this, EditTransferNoteActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, mPallet);
                intent.putExtra(GlobalVars.TRANSFER_NOTE_LIST, 0);
                startActivity(intent);
            }
        });
        binding.btnCompleting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void startTransferNoteActivity(IntentType intentType) {
        Class<?> activityClass;
        switch (intentType) {
            case EDIT_TRANSFER_NOTE:
                activityClass = EditTransferNoteActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Unsupported intent type: " + intentType);
        }
        startActivity(new Intent(this, activityClass));
    }

    private void initializePalletTransferDetails() {
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        mPallet = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);
    }

    private void initializeTransferNoteRecyclerView() {
        updateViewState(ViewState.LOADING);

        transferViewModel.getPalletTransferDetailLiveData(mPallet.getId()).observe(this, apiResponse -> {
            if (apiResponse.getData().getPalletTransfer().getTransferNotes().size() != 0) {

                setupTransferNoteAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());

                updateViewState(ViewState.DATA_AVAILABLE);
                if (apiResponse.getData().getPalletTransfer().getStatus().equals("Preparation in Progress") && apiResponse.getData().getPalletTransfer().getTransferNotes().size() != 0){
                    binding.btnCompleting.setVisibility(View.VISIBLE);
                } else {
                    binding.btnCompleting.setVisibility(View.GONE);
                }
            } else {
                updateViewState(ViewState.NO_DATA);
            }
            binding.bgStatus.setBackgroundColor(Color.parseColor("#" + apiResponse.getData().getPalletTransfer().getColorCode()));
            binding.tvStatusProgressLayer.setText(apiResponse.getData().getPalletTransfer().getStatus());
            binding.tvTransactionNumber.setText(apiResponse.getData().getPalletTransfer().getTransactionNumber());
            binding.tvPalletNo.setText(apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
            binding.tvTotalCarton.setText(apiResponse.getData().getPalletTransfer().getTotalCarton());
            binding.tvLocationFrom.setText(apiResponse.getData().getPalletTransfer().getLocationFrom());
        });
    }

    private void setupTransferNoteAdapter(List<TransferNote> transferNotes) {
        mAdapter = new TransferNoteAdapter(this, transferNotes, mPallet.getPalletSerialNumber(), new TransferNoteAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, TransferNote transferNote) {
                Intent intent = new Intent(PalletTransferDetailActivity.this, EditTransferNoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, mPallet);
                intent.putExtra(GlobalVars.TRANSFER_NOTE_LIST, transferNote);
                startActivity(intent);
            }
        });
        binding.rvTransferNote.setAdapter(mAdapter);
    }

    private void updateViewState(ViewState viewState) {
        switch (viewState) {
            case LOADING:
                binding.rvTransferNote.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.GONE);
                progressDialog = GlobalVars.pgDialog(PalletTransferDetailActivity.this);
                break;
            case DATA_AVAILABLE:
                binding.rvTransferNote.setVisibility(View.VISIBLE);
                binding.tvNoData.setVisibility(View.GONE);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                break;
            case NO_DATA:
                binding.btnCompleting.setVisibility(View.GONE);
                binding.rvTransferNote.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                break;
        }

    }

    private void showConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PalletTransferDetailActivity.this);

        alertDialogBuilder.setMessage("Are preparations complete?\n\nAfter completing preparation, you cannot change any data on this pallet.");

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss(); // Tutup dialog
            }
        });

        alertDialogBuilder.setPositiveButton("Yes Complete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                performCompletingAction();

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void performCompletingAction() {
        // ...
        transferViewModel.completePreparationLiveData(mPallet.getId()).observe(this, apiResponse -> {
            Toast.makeText(PalletTransferDetailActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}