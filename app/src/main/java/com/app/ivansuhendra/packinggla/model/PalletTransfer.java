package com.app.ivansuhendra.packinggla.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PalletTransfer implements Parcelable {
    private int id;
    @SerializedName("transaction_number")
    private String transactionNumber;
    @SerializedName("pallet_serial_number")
    private String palletSerialNumber;
    @SerializedName("total_carton")
    private String totalCarton;
    @SerializedName("location_from")
    private String locationFrom;
    private String status;
    @SerializedName("transfer_notes")
    private ArrayList<TransferNote> transferNotes;

    public int getId() {
        return id;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getPalletSerialNumber() {
        return palletSerialNumber;
    }

    public String getTotalCarton() {
        return totalCarton;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public ArrayList<TransferNote> getTransferNotes() {
        return transferNotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.transactionNumber);
        dest.writeString(this.palletSerialNumber);
        dest.writeString(this.totalCarton);
        dest.writeString(this.locationFrom);
        dest.writeString(this.status);
        dest.writeList(this.transferNotes);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.transactionNumber = source.readString();
        this.palletSerialNumber = source.readString();
        this.totalCarton = source.readString();
        this.locationFrom = source.readString();
        this.status = source.readString();
        this.transferNotes = new ArrayList<TransferNote>();
        source.readList(this.transferNotes, TransferNote.class.getClassLoader());
    }

    public PalletTransfer() {
    }

    protected PalletTransfer(Parcel in) {
        this.id = in.readInt();
        this.transactionNumber = in.readString();
        this.palletSerialNumber = in.readString();
        this.totalCarton = in.readString();
        this.locationFrom = in.readString();
        this.status = in.readString();
        this.transferNotes = new ArrayList<TransferNote>();
        in.readList(this.transferNotes, TransferNote.class.getClassLoader());
    }

    public static final Creator<PalletTransfer> CREATOR = new Creator<PalletTransfer>() {
        @Override
        public PalletTransfer createFromParcel(Parcel source) {
            return new PalletTransfer(source);
        }

        @Override
        public PalletTransfer[] newArray(int size) {
            return new PalletTransfer[size];
        }
    };
}
