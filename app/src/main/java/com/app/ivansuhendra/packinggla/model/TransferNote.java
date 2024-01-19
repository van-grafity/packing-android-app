package com.app.ivansuhendra.packinggla.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransferNote implements Parcelable {
    private int id;
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("issued_by")
    private String issuedBy;
    @SerializedName("authorized_by")
    private String authBy;
    @SerializedName("total_carton")
    private String totalCarton;
    @SerializedName("received_by")
    private String receiveBy;
    @SerializedName("received_at")
    private String receiveAt;
    @SerializedName("created_at")
    private String createAt;
    @SerializedName("cartons")
    private ArrayList<Carton> carton;

    public int getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public String getAuthBy() {
        return authBy;
    }

    public String getTotalCarton() {
        return totalCarton;
    }

    public String getReceiveBy() {
        return receiveBy;
    }

    public String getReceiveAt() {
        return receiveAt;
    }

    public String getCreateAt() {
        return createAt;
    }

    public ArrayList<Carton> getCarton() {
        return carton;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.serialNumber);
        dest.writeString(this.issuedBy);
        dest.writeString(this.authBy);
        dest.writeString(this.totalCarton);
        dest.writeString(this.receiveBy);
        dest.writeString(this.receiveAt);
        dest.writeString(this.createAt);
        dest.writeList(this.carton);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.serialNumber = source.readString();
        this.issuedBy = source.readString();
        this.authBy = source.readString();
        this.totalCarton = source.readString();
        this.receiveBy = source.readString();
        this.receiveAt = source.readString();
        this.createAt = source.readString();
        this.carton = new ArrayList<Carton>();
        source.readList(this.carton, Carton.class.getClassLoader());
    }

    public TransferNote() {
    }

    protected TransferNote(Parcel in) {
        this.id = in.readInt();
        this.serialNumber = in.readString();
        this.issuedBy = in.readString();
        this.authBy = in.readString();
        this.totalCarton = in.readString();
        this.receiveBy = in.readString();
        this.receiveAt = in.readString();
        this.createAt = in.readString();
        this.carton = new ArrayList<Carton>();
        in.readList(this.carton, Carton.class.getClassLoader());
    }

    public static final Creator<TransferNote> CREATOR = new Creator<TransferNote>() {
        @Override
        public TransferNote createFromParcel(Parcel source) {
            return new TransferNote(source);
        }

        @Override
        public TransferNote[] newArray(int size) {
            return new TransferNote[size];
        }
    };
}
