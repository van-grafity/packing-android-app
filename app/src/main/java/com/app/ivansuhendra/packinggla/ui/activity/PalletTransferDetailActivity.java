package com.app.ivansuhendra.packinggla.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
    private int mId;
    private String palletSerialNumber;

    public String getPalletSerialNumber() {
        return palletSerialNumber;
    }

    public void setPalletSerialNumber(String palletSerialNumber) {
        this.palletSerialNumber = palletSerialNumber;
    }

    private enum IntentType {
        EDIT_TRANSFER_NOTE,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViews();
        initializeClickListeners();

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

//        https://github.com/greatyao/v2ex-android/blob/f7bdb02e2ecbab640e77b3efcfc7382901712cdf/app/src/main/java/com/yaoyumeng/v2ex2/ui/TopicActivity.java#L32
//        https://developer.android.com/training/app-links/deep-linking.html?source=post_page-----1b9fe9e38abd--------------------------------
        Intent intent = getIntent();
        Uri data = intent.getData();
        String scheme = data != null ? data.getScheme() : ""; // "http"
        String host = data != null ? data.getHost() : ""; // "www.v2ex.com"
        List<String> pathSegments = data != null ? data.getPathSegments() : null;
        if ((scheme.equals("http")) && (host.equals("192.168.5.236")) && pathSegments != null && pathSegments.size() == 2) {
            String idString = pathSegments.get(1); // Get the last segment as the ID
            mId = Integer.parseInt(idString); // Parse the ID string to an integer
            setPalletSerialNumber("");
        } else {
            mPallet = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);
            mId = mPallet.getId();
            setPalletSerialNumber(mPallet.getPalletSerialNumber());
        }

        initializeTransferNoteRecyclerView(mId);
    }

    private void initializeViews() {
        binding = ActivityPalletTransferDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.rvTransferNote.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        initializeTransferNoteRecyclerView(mId);

        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void initializeClickListeners() {
        binding.btnItemTransferNote.setOnClickListener(view -> startTransferNoteActivity(IntentType.EDIT_TRANSFER_NOTE));
        binding.btnNewTransferNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PalletTransferDetailActivity.this, TransferNoteActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, mPallet);
                intent.putExtra(GlobalVars.TRANSFER_NOTE_LIST, 0);
                startActivity(intent);
                finish();
            }
        });
        binding.btnCompleting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startTransferNoteActivity(IntentType intentType) {
        Class<?> activityClass;
        switch (intentType) {
            case EDIT_TRANSFER_NOTE:
                activityClass = TransferNoteActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Unsupported intent type: " + intentType);
        }
        startActivity(new Intent(this, activityClass));
    }

    private void initializeTransferNoteRecyclerView(int id) {
        updateViewState(ViewState.LOADING);

        transferViewModel.getPalletTransferDetailLiveData(id).observe(this, apiResponse -> {
            if (apiResponse.getData().getPalletTransfer().getTransferNotes().size() != 0) {

                setupTransferNoteAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());

                updateViewState(ViewState.DATA_AVAILABLE);
//                && apiResponse.getData().getPalletTransfer().getTransferNotes().size() != 0
                if (apiResponse.getData().getPalletTransfer().getStatus().equals("Preparation in Progress")){
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
        mAdapter = new TransferNoteAdapter(this, transferNotes, getPalletSerialNumber(), new TransferNoteAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, TransferNote transferNote) {
                Intent intent = new Intent(PalletTransferDetailActivity.this, TransferNoteActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, mPallet);
                intent.putExtra(GlobalVars.TRANSFER_NOTE_LIST, transferNote);
                startActivity(intent);
                finish();
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
        transferViewModel.completePreparationLiveData(mId).observe(this, apiResponse -> {
            Toast.makeText(PalletTransferDetailActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}