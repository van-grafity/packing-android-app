package com.app.ivansuhendra.packinggla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.ivansuhendra.packinggla.databinding.ActivityNewPalletTransferBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.Location;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.PalletTransferViewModel;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

import java.util.ArrayList;

public class NewPalletTransferActivity extends AppCompatActivity {
    private static final String TAG = "NewPalletTransfer";
    private ActivityNewPalletTransferBinding binding;
    private TransferViewModel transferViewModel;
    private int locationFromId;
    private int locationToId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPalletTransferBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        String mSn;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mSn = "";
            } else {
                mSn = extras.getString("sn");
            }
        } else {
            mSn = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        binding.etSerialNumber.setText(mSn);

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

        initData();

        binding.spLocationFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Location selectedItem = (Location) parent.getItemAtPosition(position);
                locationFromId = selectedItem.getId();
                String selectedName = selectedItem.getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spLocationTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Location selectedItem = (Location) parent.getItemAtPosition(position);
                locationToId = selectedItem.getId();
                String selectedName = selectedItem.getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String palletSerialNumber = binding.etSerialNumber.getText().toString();
                showLoadingAndDelayApiCall(palletSerialNumber);
            }
        });
    }

    private void showLoadingAndDelayApiCall(String palletSerialNumber) {
        binding.content.setVisibility(View.GONE);
        binding.action.setVisibility(View.GONE);
        binding.contentLoad.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, make the API call
                makeApiCall(palletSerialNumber);
            }
        }, 1500);
    }

    private void makeApiCall(String palletSerialNumber) {
        transferViewModel.savePalletTransferLiveData(palletSerialNumber, locationFromId, locationToId)
                .observe(NewPalletTransferActivity.this, new Observer<APIResponse>() {
                    @Override
                    public void onChanged(APIResponse apiResponse) {
                        binding.contentLoad.setVisibility(View.GONE);
                        binding.content.setVisibility(View.VISIBLE);
                        binding.action.setVisibility(View.VISIBLE);

                        if (apiResponse == null) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewPalletTransferActivity.this);
                            alertDialogBuilder
                                    .setMessage("This Pallet has been used. Please Check on Pallet to Transfer List")
                                    .setCancelable(false)
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();                        } else {
                            Toast.makeText(NewPalletTransferActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void initData() {
        initializeDataLocationFrom();
        initializeDataLocationTo();
    }

    private void initializeDataLocationFrom() {
        ArrayList<Location> mItemFrom = new ArrayList<>();
        mItemFrom.add(new Location(1, "Factory A"));
        mItemFrom.add(new Location(2, "Factory B"));
        mItemFrom.add(new Location(3, "Warehouse"));

        ArrayAdapter<Location> fromAdapter = new ArrayAdapter<>(NewPalletTransferActivity.this, android.R.layout.simple_spinner_item, mItemFrom);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spLocationFrom.setAdapter(fromAdapter);
    }

    private void initializeDataLocationTo() {
        ArrayList<Location> mItemTo = new ArrayList<>();
        mItemTo.add(new Location(1, "Factory A"));
        mItemTo.add(new Location(2, "Factory B"));
        mItemTo.add(new Location(3, "Warehouse"));

        ArrayAdapter<Location> toAdapter = new ArrayAdapter<>(NewPalletTransferActivity.this, android.R.layout.simple_spinner_item, mItemTo);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spLocationTo.setAdapter(toAdapter);
    }
}