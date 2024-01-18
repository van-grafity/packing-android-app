package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIModels {
    @SerializedName("pallet_transfers")
    private ArrayList<PalletTransfer> palletTransfers;

    @SerializedName("pallet_transfer")
    private PalletTransfer palletTransfer;

    public ArrayList<PalletTransfer> getPalletTransfers() {
        return palletTransfers;
    }

    public PalletTransfer getPalletTransfer() {
        return palletTransfer;
    }
}
