package com.app.ivansuhendra.packinggla;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.adapter.TransferNoteAdapter;
import com.app.ivansuhendra.packinggla.databinding.ActivityPalletTransferDetailBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
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

    private enum IntentType {
        EDIT_TRANSFER_NOTE,
        NEW_TRANSFER_NOTE
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
    }

    private void initializeClickListeners() {
        binding.btnItemTransferNote.setOnClickListener(view -> startTransferNoteActivity(IntentType.EDIT_TRANSFER_NOTE));
        binding.btnNewTransferNote.setOnClickListener(view -> startTransferNoteActivity(IntentType.NEW_TRANSFER_NOTE));
    }

    private void startTransferNoteActivity(IntentType intentType) {
        Class<?> activityClass;
        switch (intentType) {
            case EDIT_TRANSFER_NOTE:
                activityClass = EditTransferNoteActivity.class;
                break;
            case NEW_TRANSFER_NOTE:
                activityClass = NewTransferNoteActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Unsupported intent type: " + intentType);
        }
        startActivity(new Intent(this, activityClass));
    }

    private void initializePalletTransferDetails() {
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        mPallet = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);

        binding.bgStatus.setBackground(getDrawable(GlobalVars.provideStatus(mPallet.getStatus())));
        binding.tvStatusProgressLayer.setText(mPallet.getStatus());
        binding.tvTransactionNumber.setText(mPallet.getTransactionNumber());
        binding.tvPalletNo.setText(mPallet.getPalletSerialNumber());
        binding.tvTotalCarton.setText(mPallet.getTotalCarton());
        binding.tvLocationFrom.setText(mPallet.getLocationFrom());
    }

    private void initializeTransferNoteRecyclerView() {
        updateViewState(ViewState.LOADING);

        transferViewModel.getPalletTransferDetailLiveData(mPallet.getId()).observe(this, apiResponse -> {
            if (apiResponse.getData().getPalletTransfer().getTransferNotes().size() != 0) {
                setupTransferNoteAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes());
                updateViewState(ViewState.DATA_AVAILABLE);
            } else {
                updateViewState(ViewState.NO_DATA);
            }
        });
    }

    private void setupTransferNoteAdapter(List<TransferNote> transferNotes) {
        mAdapter = new TransferNoteAdapter(this, transferNotes, mPallet.getPalletSerialNumber(), new TransferNoteAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, TransferNote transferNote) {
                Intent intent = new Intent(PalletTransferDetailActivity.this, EditTransferNoteActivity.class);
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
                binding.contentLoading.setVisibility(View.VISIBLE);
                binding.contentLoading.show();
                binding.rvTransferNote.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.GONE);
                break;
            case DATA_AVAILABLE:
                binding.rvTransferNote.setVisibility(View.VISIBLE);
                binding.tvNoData.setVisibility(View.GONE);
                binding.contentLoading.hide();
                binding.contentLoading.setVisibility(View.GONE);
                break;
            case NO_DATA:
                binding.rvTransferNote.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.contentLoading.hide();
                binding.contentLoading.setVisibility(View.GONE);
                break;
        }
    }
}