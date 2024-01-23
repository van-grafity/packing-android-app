package com.app.ivansuhendra.packinggla;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.adapter.TransferNoteAdapter;
import com.app.ivansuhendra.packinggla.databinding.ActivityPalletReceiveDetailBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.Rack;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

import java.util.ArrayList;
import java.util.List;

public class PalletReceiveDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ActivityPalletReceiveDetailBinding binding;
    private TransferViewModel transferViewModel;
    private ProgressDialog progressDialog;
    private TransferNoteAdapter mAdapter;
    private String mSn;
    private int palletTransferId;
    private String palletBarcode;
    private ArrayList<Rack> items;
    private ArrayAdapter<Rack> rackAdapter;
    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPalletReceiveDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        progressDialog = GlobalVars.pgDialog(PalletReceiveDetailActivity.this);
        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        binding.spRack.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            mSn = (extras != null) ? extras.getString("sn", "") : "";
        } else {
            mSn = savedInstanceState.getString("STRING_I_NEED", "");
        }

        initViews();
        initData();

        binding.btnSendPallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                createPalletReceive();
            }
        });
    }

    private void provideRack() {
        items = new ArrayList<>();
        items.add(new Rack(1, "Rack 1"));
        items.add(new Rack(2, "Rack 2"));
        items.add(new Rack(3, "Rack 3"));

        rackAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        rackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRack.setAdapter(rackAdapter);
        
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedId = items.get(position).getId();
        Toast.makeText(this, ""+selectedId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initViews() {
        binding.rvTransferNote.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
    }

    private void initData() {
        transferViewModel.searchPalletReceiveLiveData(mSn).observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {

                updateUI(apiResponse);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

    private void updateUI(APIResponse apiResponse) {
//        Toast.makeText(PalletReceiveDetailActivity.this, ""+apiResponse.getData().getPalletTransfer().getPalletTransferId(), Toast.LENGTH_SHORT).show();
        // Update UI based on API response
        binding.tvPalletTransferNo.setText(apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
//        binding.bgStatus.setBackground(getDrawable(GlobalVars.provideStatus(apiResponse.getData().getPalletTransfer().getStatus())));
        binding.bgStatus.setBackgroundColor(Color.parseColor("#"+apiResponse.getData().getPalletTransfer().getColorCode()));
        binding.tvStatusProgressLayer.setText(apiResponse.getData().getPalletTransfer().getStatus());
        binding.tvPalletNo.setText(apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
        binding.tvTotalCarton.setText(apiResponse.getData().getPalletTransfer().getTotalCarton());
        binding.tvLocationFrom.setText(apiResponse.getData().getPalletTransfer().getLocationFrom());
        palletTransferId = apiResponse.getData().getPalletTransfer().getPalletTransferId(); // Obtain the pallet transfer ID;
        palletBarcode = apiResponse.getData().getPalletTransfer().getPalletSerialNumber();
        provideRack();
        setupTransferNoteAdapter(apiResponse.getData().getPalletTransfer().getTransferNotes(), apiResponse.getData().getPalletTransfer().getPalletSerialNumber());
    }

    private void createPalletReceive() {
        String receivedBy = "Andre"; // Obtain the received by information;
        transferViewModel.createPalletReceiveLiveData(palletTransferId, selectedId, receivedBy, palletBarcode).observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                // Handle the response as needed
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PalletReceiveDetailActivity.this);
                alertDialogBuilder
                        .setMessage(apiResponse.getMessage())
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

    private void setupTransferNoteAdapter(List<TransferNote> transferNotes, String palletNo) {
        mAdapter = new TransferNoteAdapter(this, transferNotes, palletNo, new TransferNoteAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, TransferNote transferNote) {
                Intent intent = new Intent(PalletReceiveDetailActivity.this, EditTransferNoteActivity.class);
                startActivity(intent);
            }
        });
        binding.rvTransferNote.setAdapter(mAdapter);
    }
}