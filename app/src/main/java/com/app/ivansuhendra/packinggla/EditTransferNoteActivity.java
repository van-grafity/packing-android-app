package com.app.ivansuhendra.packinggla;

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
import android.os.Handler;
import android.os.Looper;
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
import com.app.ivansuhendra.packinggla.databinding.ActivityEditTransferNoteBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.Carton;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.model.TransferNote;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EditTransferNoteActivity extends AppCompatActivity {

    private ActivityEditTransferNoteBinding binding;
    private static final int REQUEST_SCAN_QR = 1; // Unique request code
    private TransferNote transferNote;
    private PalletTransfer palletTransfer;
    private TransferViewModel transferViewModel;
    private CartonAdapter mAdapter;
    private ProgressDialog progressDialog;
    private PackingDBHelper mDbHelper;
    private Integer[] barcodeId;
    private ArrayList<Carton> cartons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTransferNoteBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        transferNote = getIntent().getParcelableExtra(GlobalVars.TRANSFER_NOTE_LIST);
        palletTransfer = getIntent().getParcelableExtra(GlobalVars.PALLET_TRANSFER_LIST);
        mDbHelper = new PackingDBHelper(this);
        clearDatabase();
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

        initializeData();

        binding.btnAddCarton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTransferNoteActivity.this, ScanQrActivity.class);
                startActivityForResult(intent, REQUEST_SCAN_QR);
            }
        });

        // Call the modified checkAndLoadData method
        checkAndLoadDataWithDelay();

        // referensi
        // Integer[] barcodeId = new Integer[1];
        // barcodeId[0] = 2260;
        // binding.btnEditTransferNote.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         transferViewModel.updateTransferNoteLiveData(1, 6, barcodeId).observe(EditTransferNoteActivity.this, new Observer<APIResponse>() {
        //             @Override
        //             public void onChanged(APIResponse apiResponse) {
        //                 Toast.makeText(EditTransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
        //                 if (progressDialog != null && progressDialog.isShowing()) {
        //                     progressDialog.cancel();
        //                 }
        //             }
        //         });
        //     }
        // });

        // id carton yang ada di database sekarang
        binding.btnEditTransferNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); // Show loading indicator
                barcodeId = new Integer[mDbHelper.getAllCarton().size()];
                for (int i = 0; i < mDbHelper.getAllCarton().size(); i++) {
                    barcodeId[i] = mDbHelper.getAllCarton().get(i).getId();
                }

                if (transferNote == null) {
                    Integer[] cartonsArray = new Integer[cartons.size()];
                    for (int i = 0; i < cartons.size(); i++) {
                        cartonsArray[i] = cartons.get(i).getId();
                    }

                    // Toast.makeText(EditTransferNoteActivity.this, ""+cartonsArray.length, Toast.LENGTH_SHORT).show();
                    // if (progressDialog != null && progressDialog.isShowing()) {
                    //     progressDialog.cancel();
                    // }
                    
                    transferViewModel.newTransferNoteLiveData(palletTransfer.getId(), cartonsArray).observe(EditTransferNoteActivity.this, new Observer<APIResponse>() {
                        @Override
                        public void onChanged(APIResponse apiResponse) {
                            Toast.makeText(EditTransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditTransferNoteActivity.this, PalletTransferDetailActivity.class);
                            intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, palletTransfer);
                            startActivity(intent);
                            finish();
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
                        }
                    });
                } else {
                    // Toast.makeText(EditTransferNoteActivity.this, ""+barcodeId.length, Toast.LENGTH_SHORT).show();
                    // if (progressDialog != null && progressDialog.isShowing()) {
                    //     progressDialog.cancel();
                    // }
                    transferViewModel.updateTransferNoteLiveData(palletTransfer.getId(), transferNote.getId(), barcodeId).observe(EditTransferNoteActivity.this, new Observer<APIResponse>() {
                        @Override
                        public void onChanged(APIResponse apiResponse) {
                            Toast.makeText(EditTransferNoteActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditTransferNoteActivity.this, PalletTransferDetailActivity.class);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SCAN_QR && resultCode == RESULT_OK) {
            String scannedResult = data.getStringExtra("scannedResult");
            // Process the scannedResult as needed in EditTransferNoteActivity
            progressDialog.show(); // Show loading indicator
            transferViewModel.searchCartonLiveData(scannedResult).observe(EditTransferNoteActivity.this, new Observer<APIResponse>() {
                @Override
                public void onChanged(APIResponse apiResponse) {
                    progressDialog.show(); // Show loading indicator
                    if (apiResponse.getStatus().equals("success")) {
                        // insert ke database dan update adapter
                        mDbHelper.insertCarton(apiResponse.getData().getCarton());
                        provideDbDataFromServer();

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
                            cartons.add(carton);
                            mAdapter.setData(cartons);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(EditTransferNoteActivity.this, "This carton is already in the list", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }

                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditTransferNoteActivity.this);
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
        for (Carton existingCarton : cartons) {
            // Compare based on some unique identifier, for example, carton ID or barcode
            if (existingCarton.getId() == carton.getId()) {
                return true; // Carton already exists in the list
            }
        }
        return false; // Carton not found in the list
    }

    private void checkAndLoadDataWithDelay() {
        // Create a Handler on the main thread
        Handler handler = new Handler(Looper.getMainLooper());

        // Use the handler to delay the loading of data for a specific duration (e.g., 2000 milliseconds)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndLoadData();
            }
        }, 2000); // Adjust the delay duration as needed
    }

    private void checkAndLoadData() {
        if (isNetworkConnected()) {
            // If connected to the network, proceed with loading data
            provideDbDataFromServer();
        } else {
            // If not connected, show Snackbar with refresh action
            showNoNetworkSnackbar();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
    }

    private void loadDataFromServer() {
        progressDialog.show(); // Show loading indicator

        transferViewModel.getPalletTransferNoteLiveData(transferNote.getId()).observe(this, new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                // Update the adapter data and notify the changes
                mAdapter.setData(apiResponse.getData().getTransferNote().getCarton());
                mAdapter.notifyDataSetChanged();

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

    private void provideDbDataFromServer() {
        progressDialog.show(); // Show loading indicator

        if (transferNote == null) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }else {
            transferViewModel.getPalletTransferNoteLiveData(transferNote.getId()).observe(this, new Observer<APIResponse>() {
                @Override
                public void onChanged(APIResponse apiResponse) {
                    for (Carton carton : apiResponse.getData().getTransferNote().getCarton()) {
                        mDbHelper.insertCarton(carton);
                    }
                    // Update the adapter data and notify the changes
                    mAdapter.setData(mDbHelper.getAllCarton());
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
                // When the user clicks on refresh, check network and reload data
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

    private void initializeData() {
        binding.rvCarton.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        progressDialog = GlobalVars.pgDialog(EditTransferNoteActivity.this);

        // Initialize the adapter here
        mAdapter = new CartonAdapter(EditTransferNoteActivity.this, new ArrayList<>(), new CartonAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int position, Carton carton) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTransferNoteActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah Anda yakin ingin menghapus item ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.removeItem(position);
                        mDbHelper.deleteCarton(carton.getId());
                        mAdapter.notifyDataSetChanged();
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
        clearDatabase();
    }

    private void clearDatabase() {
        // Delete all data from the "carton" table in the database
        mDbHelper.clearCartonTable();
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
                    carton.setPcs(cursor.getString(10));
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