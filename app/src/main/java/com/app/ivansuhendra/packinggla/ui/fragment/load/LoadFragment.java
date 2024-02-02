package com.app.ivansuhendra.packinggla.ui.fragment.load;

import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ivansuhendra.packinggla.ui.activity.PalletLoadActivity;
import com.app.ivansuhendra.packinggla.databinding.FragmentLoadBinding;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.budiyev.android.codescanner.CodeScanner;

public class LoadFragment extends Fragment {

    private static final String TAG = "ScanQrFragment";
    private final int CAMERA_REQUEST_CODE = 101;
    private FragmentLoadBinding binding;  // Update binding type
    private CodeScanner mCodeScanner;
    private String serialCode;

    public static LoadFragment newInstance() {
        return new LoadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoadBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPermissions();

        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if (extras == null) {
                serialCode = "";
            } else {
                serialCode = extras.getString(GlobalVars.PLT_CODE);
            }
        } else {
            serialCode = savedInstanceState.getString("STRING_I_NEED", "");
        }

        mCodeScanner = new CodeScanner(requireContext(), binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> {
            requireActivity().runOnUiThread(() -> {
                String message = result.getText();

                Intent intent = new Intent(requireContext(), PalletLoadActivity.class);
                intent.putExtra("scannedLoad", message);
                startActivity(intent);
            });
        });
    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Handle permission results here
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
        Log.i(TAG, "onPause: unregisterReceiver");
    }
}