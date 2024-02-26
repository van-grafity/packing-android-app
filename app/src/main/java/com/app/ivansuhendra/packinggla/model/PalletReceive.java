package com.app.ivansuhendra.packinggla.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PalletReceive implements Parcelable {
    private int id;
    @SerializedName("pallet_transfer_id")
    private int palletTransferId;
    @SerializedName("transaction_number")
    private String transactionNumber;
    @SerializedName("pallet_serial_number")
    private String palletSerialNumber;
    @SerializedName("total_carton")
    private String totalCarton;
    @SerializedName("location_from")
    private String locationFrom;
    @SerializedName("location_to")
    private String locationTo;
    private String status;
    @SerializedName("transfer_notes")
    private ArrayList<TransferNote> transferNotes;
    @SerializedName("color_hex")
    private String colorCode;
    @SerializedName("rack_serial_number")
    private String rackNo;

    public int getId() {
        return id;
    }

    public int getPalletTransferId() {
        return palletTransferId;
    }

    public String getTransactionNumber() {
        return transactionNumber;
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

    public String getLocationTo() {
        return locationTo;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<TransferNote> getTransferNotes() {
        return transferNotes;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getRackNo() {
        return rackNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.palletTransferId);
        dest.writeString(this.transactionNumber);
        dest.writeString(this.palletSerialNumber);
        dest.writeString(this.totalCarton);
        dest.writeString(this.locationFrom);
        dest.writeString(this.locationTo);
        dest.writeString(this.status);
        dest.writeTypedList(this.transferNotes);
        dest.writeString(this.colorCode);
        dest.writeString(this.rackNo);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.palletTransferId = source.readInt();
        this.transactionNumber = source.readString();
        this.palletSerialNumber = source.readString();
        this.totalCarton = source.readString();
        this.locationFrom = source.readString();
        this.locationTo = source.readString();
        this.status = source.readString();
        this.transferNotes = source.createTypedArrayList(TransferNote.CREATOR);
        this.colorCode = source.readString();
        this.rackNo = source.readString();
    }

    public PalletReceive() {
    }

    protected PalletReceive(Parcel in) {
        this.id = in.readInt();
        this.palletTransferId = in.readInt();
        this.transactionNumber = in.readString();
        this.palletSerialNumber = in.readString();
        this.totalCarton = in.readString();
        this.locationFrom = in.readString();
        this.locationTo = in.readString();
        this.status = in.readString();
        this.transferNotes = in.createTypedArrayList(TransferNote.CREATOR);
        this.colorCode = in.readString();
        this.rackNo = in.readString();
    }

    public static final Creator<PalletReceive> CREATOR = new Creator<PalletReceive>() {
        @Override
        public PalletReceive createFromParcel(Parcel source) {
            return new PalletReceive(source);
        }

        @Override
        public PalletReceive[] newArray(int size) {
            return new PalletReceive[size];
        }
    };
}
