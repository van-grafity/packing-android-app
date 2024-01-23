package com.app.ivansuhendra.packinggla;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.ivansuhendra.packinggla.databinding.ActivityScanQrBinding;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScanQrActivity extends AppCompatActivity {
    private static final String TAG = "ScanQrActivity";
    private final int CAMERA_REQUEST_CODE = 101;
    private ActivityScanQrBinding binding;
    private CodeScanner mCodeScanner;
    private String serialCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrBinding.inflate(LayoutInflater.from(ScanQrActivity.this));
        setContentView(binding.getRoot());
        setupPermissions();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                serialCode = "";
            } else {
                serialCode = extras.getString(GlobalVars.PLT_CODE);
            }
        } else {
            serialCode = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = result.getText();
                        String partStr = message.substring(0, 4);

//                        Toast.makeText(ScanQrActivity.this, serialCode, Toast.LENGTH_SHORT).show();
                        if (serialCode.equals("PLT_CODE")){
                            Intent intent = new Intent(ScanQrActivity.this, NewPalletTransferActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("sn", message);
                            startActivity(intent);
                            finish();
                        } else if (serialCode.equals("RECEIVE_CODE")){
                            Intent intent = new Intent(ScanQrActivity.this, PalletReceiveDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("sn", message);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("scannedResult", message);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void addLocationMachine(View view) {

    }

    private void setupPermissions() {
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    /**
     * Requests permission to access the camera
     * @param requestCode is the request code
     * @param permissions is a string that seeks to access the camera
     * @param grantResults is a request code
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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