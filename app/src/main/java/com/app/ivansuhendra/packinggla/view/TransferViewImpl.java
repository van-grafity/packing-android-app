package com.app.ivansuhendra.packinggla.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ProgressBar;

import com.app.ivansuhendra.packinggla.MainActivity;
import com.app.ivansuhendra.packinggla.adapter.PalletTransferfAdapter;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;

import java.util.List;

public class TransferViewImpl implements TransferView{
    private Activity activity;
    private ProgressDialog progressDialog;
    private PalletTransferfAdapter adapter;

    public TransferViewImpl(Activity activity, PalletTransferfAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void showProgress() {
        progressDialog = GlobalVars.pgDialog(activity);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showPalletTransfers(List<PalletTransfer> palletTransfers) {
        adapter.setPalletTransfers(palletTransfers);
    }

    @Override
    public void addPalletTransfers(List<PalletTransfer> palletTransfers) {
        adapter.addPalletTransfers(palletTransfers);
    }
}
