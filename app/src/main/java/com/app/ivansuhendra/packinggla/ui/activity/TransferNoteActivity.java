package com.app.ivansuhendra.packinggla.ui.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.adapter.CartonAdapter;
import com.app.ivansuhendra.packinggla.databinding.ActivityTransferNoteBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.Carton;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TransferNoteActivity extends AppCompatActivity {

    private ActivityTransferNoteBinding binding;
    private static final int REQUEST_SCAN_QR = 1; // Unique request code
    private TransferNote transferNote;
    private PalletTransfer palletTransfer;
    private TransferViewModel transferViewModel;
    private CartonAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Carton> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        transferNote = getIntent().getParcelableExtra(GlobalVars.TRANSFER_NOTE_LIST);
        palletTransfer = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);

        if (transferNote == null) {
            binding.tvTitle.setText("New Transfer Note");
            binding.btnEditTransferNote.setText("New Transfer Note");
            binding.tvTransferNoteSerial.setText("No.-");
        } else {
            binding.tvTitle.setText("Edit Transfer Note");
            binding.btnEditTransferNote.setText("Edit Transfer Note");
            binding.tvTransferNoteSerial.setText(transferNote.getSerialNumber());
        }
        binding.tvFrom.setText(palletTransfer.getLocationFrom());
        binding.tvPalletNo.setText(palletTransfer.getPalletSerialNumber());
        binding.tvTotalCarton.setText(palletTransfer.getTotalCarton());

        initializeView();

        checkAndLoadData();

        binding.btnAddCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransferNoteActivity.this, ScanQrActivity.class);
                startActivityForResult(intent, REQUEST_SCAN_QR);
            }
        });

        binding.btnEditTransferNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); // Show loading indicator
                Integer[] cartonsArray = new Integer[mDataList.size()];
                for (int i = 0; i < mDataList.size(); i++) {
                    cartonsArray[i] = mDataList.get(i).getId();
                }
                if (transferNote == null) {
                    transferViewModel.newTransferNoteLiveData(palletTransfer.getId(), cartonsArray).observe(TransferNoteActivity.this, new Observer<APIResponse>() {
                        @Override
                        public void onChanged(APIResponse apiResponse) {
                            Toast.makeText(TransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransferNoteActivity.this, PalletTransferDetailActivity.class);
                            intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, palletTransfer);
                            startActivity(intent);
                            finish();
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
                        }
                    });
                } else {
                    transferViewModel.updateTransferNoteLiveData(palletTransfer.getId(), transferNote.getId(), cartonsArray).observe(TransferNoteActivity.this, new Observer<APIResponse>() {
                        @Override
                        public void onChanged(APIResponse apiResponse) {
                            Toast.makeText(TransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransferNoteActivity.this, PalletTransferDetailActivity.class);
                            intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, palletTransfer);
                            startActivity(intent);
                            finish();
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
                        }
                    });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_QR && resultCode == RESULT_OK) {
            String scannedResult = data.getStringExtra("scannedResult");
            progressDialog.show();
            transferViewModel.searchCartonLiveData(scannedResult).observe(TransferNoteActivity.this, new Observer<APIResponse>() {
                @Override
                public void onChanged(APIResponse apiResponse) {
                    progressDialog.show();
                    if (apiResponse.getStatus().equals("success")) {
                        Carton carton = new Carton();
                        carton.setId(apiResponse.getData().getCarton().getId());
                        carton.setCartonBarcode(apiResponse.getData().getCarton().getCartonBarcode());
                        carton.setPoNo(apiResponse.getData().getCarton().getPoNo());
                        carton.setPackingListId(apiResponse.getData().getCarton().getPackingListId());
                        carton.setPlNo(apiResponse.getData().getCarton().getPlNo());
                        carton.setCartonNo(apiResponse.getData().getCarton().getCartonNo());
                        carton.setGlNo(apiResponse.getData().getCarton().getGlNo());
                        carton.setBuyer(apiResponse.getData().getCarton().getBuyer());
                        carton.setSeason(apiResponse.getData().getCarton().getSeason());
                        carton.setContent(apiResponse.getData().getCarton().getContent());
                        carton.setPcs(apiResponse.getData().getCarton().getPcs());
                        carton.setPacked(apiResponse.getData().getCarton().getPacked());

                        if (!isCartonInList(carton)) {
                            mDataList.add(carton);
                            mAdapter.setData(mDataList);
                            mAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(TransferNoteActivity.this, ScanQrActivity.class);
                            startActivityForResult(intent, REQUEST_SCAN_QR);
                        } else {
                            Toast.makeText(TransferNoteActivity.this, "This carton is already in the list", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }

                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TransferNoteActivity.this);
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

    private boolean isCartonInList(Carton carton) {
        for (Carton existingCarton : mDataList) {
            // Compare based on some unique identifier, for example, carton ID or barcode
            if (existingCarton.getId() == carton.getId()) {
                return true; // Carton already exists in the list
            }
        }
        return false; // Carton not found in the list
    }

    private void checkAndLoadData() {
        if (isNetworkConnected()) {
            provideDbDataFromServer();
        } else {
            showNoNetworkSnackbar();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
    }

    private void provideDbDataFromServer() {
        progressDialog.show();

        if (transferNote == null) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        } else {
            transferViewModel.getPalletTransferNoteLiveData(transferNote.getId()).observe(this, new Observer<APIResponse>() {
                @Override
                public void onChanged(APIResponse apiResponse) {
                    for (Carton carton : apiResponse.getData().getTransferNote().getCarton()) {
                        mDataList.add(carton);
                    }
                    // Update the adapter data and notify the changes
                    mAdapter.setData(mDataList);
                    mAdapter.notifyDataSetChanged();

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }
            });
        }
    }

    private void showNoNetworkSnackbar() {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "No internet connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Refresh", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndLoadData();
            }
        });
        snackbar.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    private void initializeView() {
        binding.rvCarton.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        progressDialog = GlobalVars.pgDialog(TransferNoteActivity.this);

        mAdapter = new CartonAdapter(TransferNoteActivity.this, new ArrayList<>(), new CartonAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, Carton carton) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TransferNoteActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin menghapus item ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataList.remove(position);
                        mAdapter.setData(mDataList);
                        mAdapter.notifyDataSetChanged();
                        // dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        binding.rvCarton.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferNoteActivity.this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah Anda yakin ingin membatalkan perubahan?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TransferNoteActivity.this, PalletTransferDetailActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, palletTransfer);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Tidak", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "PackingGLA.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS carton (carton_id INTEGER PRIMARY KEY AUTOINCREMENT, carton_barcode TEXT, po_number TEXT, packinglist_id INTEGER, pl_number TEXT, carton_number TEXT, gl_number TEXT, buyer_name TEXT, season TEXT, content TEXT, total_pcs INTEGER, flag_packed TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS carton");
            onCreate(db);
        }
    }

    public class PackingDBHelper extends DBHelper {
        public PackingDBHelper(Context context) {
            super(context);
        }

        // pertama di load data dari server ke db
        public void insertCarton(Carton carton) {
            if (!isCartonExist(carton)) { // Check if carton already exists
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("carton_id", carton.getId());
                contentValues.put("carton_barcode", carton.getCartonBarcode());
                contentValues.put("po_number", carton.getPoNo());
                contentValues.put("packinglist_id", carton.getPackingListId());
                contentValues.put("pl_number", carton.getPlNo());
                contentValues.put("carton_number", carton.getCartonNo());
                contentValues.put("gl_number", carton.getGlNo());
                contentValues.put("buyer_name", carton.getBuyer());
                contentValues.put("season", carton.getSeason());
                contentValues.put("content", carton.getContent());
                contentValues.put("total_pcs", carton.getPcs());
                contentValues.put("flag_packed", carton.getPacked());
                db.insert("carton", null, contentValues);
                db.close();
            }
        }

        // kedua di load data dari db ke adapter
        public ArrayList<Carton> getAllCarton() {
            ArrayList<Carton> cartonArrayList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM carton", null);
            if (cursor.moveToFirst()) {
                do {
                    Carton carton = new Carton();
                    carton.setId(cursor.getInt(0));
                    carton.setCartonBarcode(cursor.getString(1));
                    carton.setPoNo(cursor.getString(2));
                    carton.setPackingListId(cursor.getString(3));
                    carton.setPlNo(cursor.getString(4));
                    carton.setCartonNo(cursor.getString(5));
                    carton.setGlNo(cursor.getString(6));
                    carton.setBuyer(cursor.getString(7));
                    carton.setSeason(cursor.getString(8));
                    carton.setContent(cursor.getString(9));
                    carton.setPcs(cursor.getInt(10));
                    carton.setPacked(cursor.getString(11));
                    cartonArrayList.add(carton);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return cartonArrayList;
        }

        // Check if the carton already exists in the database
        private boolean isCartonExist(Carton carton) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM carton WHERE carton_barcode = ?", new String[]{carton.getCartonBarcode()});
            boolean exists = cursor.moveToFirst(); // If cursor moves to the first row, carton exists
            cursor.close();
            db.close();
            return exists;
        }

        // delete carton where id
        public void deleteCarton(int id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("carton", "carton_id = ?", new String[]{String.valueOf(id)});
            db.close();
        }

        public void clearCartonTable() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("carton", null, null);
            db.close();
        }
    }
}