package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class APIModels {
    @SerializedName("pallet_transfers")
    private ArrayList<PalletTransfer> palletTransfers;

    @SerializedName("pallet_transfer")
    private PalletTransfer palletTransfer;

    @SerializedName("transfer_note")
    private TransferNote transferNote;

    @SerializedName("carton")
    private Carton carton;

    @SerializedName("pallet_receive_list")
    private ArrayList<PalletTransfer> palletReceive;

    public ArrayList<PalletTransfer> getPalletTransfers() {
        return palletTransfers;
    }

    public PalletTransfer getPalletTransfer() {
        return palletTransfer;
    }

    public TransferNote getTransferNote() {
        return transferNote;
    }

    public Carton getCarton() {
        return carton;
    }

    public ArrayList<PalletTransfer> getPalletReceive() {
        return palletReceive;
    }
}
