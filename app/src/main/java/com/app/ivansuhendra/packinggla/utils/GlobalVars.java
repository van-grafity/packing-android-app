package com.app.ivansuhendra.packinggla.utils;

import com.app.ivansuhendra.packinggla.R;

public class GlobalVars {
    public static final String PALLET_TRANSFER_LIST = "pallet transfer list";
    public static final String TRANSFER_NOTE_LIST = "transfer note list";
    public static final String PLT_CODE = "pallet_code";

    public static int provideStatus(String status) {
        if (status.equals("Ready to Transfer")) {
            return R.drawable.rounded_bg_ready;
        } else if (status.equals("Loaded")) {
            return R.drawable.rounded_bg_loaded;
        } else if (status.equals("At Warehouse")) {
            return R.drawable.round_bg_at_warehouse;
        } else if (status.equals("At Preparation in Progress")) {
            return R.drawable.round_bg_progress;
        } else {
            return R.drawable.round_bg_red;
        }
    }
}
