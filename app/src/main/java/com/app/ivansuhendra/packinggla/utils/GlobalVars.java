package com.app.ivansuhendra.packinggla.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.app.ivansuhendra.packinggla.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;

public class GlobalVars {
    public static final String PALLET_TRANSFER_LIST = "pallet transfer list";
    public static final String PALLET_RECEIVE_LIST = "pallet receive list";
    public static final String TRANSFER_NOTE_LIST = "transfer note list";
    public static final String PLT_CODE = "pallet_code";
    public static final String PREF_USER = "pref:user";
    public static final String PREF_USER_KEY = "user";
    public static final String PREF_CREDENTIAL = "pref:credential";
    public static final String PREF_CREDENTIAL_KEY = "credential";

    public static int provideStatus(String status) {
        if (status.equals("Ready to Transfer")) {
            return R.drawable.rounded_bg_ready;
        } else if (status.equals("Loaded")) {
            return R.drawable.rounded_bg_loaded;
        } else if (status.equals("Received at Warehouse")) {
            return R.drawable.round_bg_at_warehouse;
        } else if (status.equals("At Preparation in Progress")) {
            return R.drawable.round_bg_progress;
        } else {
            return R.drawable.round_bg_red;
        }
    }

    public static ProgressDialog pgDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        Sprite doubleBounce = new FoldingCube();
        progressDialog.setIndeterminateDrawable(doubleBounce);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
