package com.app.ivansuhendra.packinggla.model;

import com.google.gson.annotations.SerializedName;

public class Carton {
    @SerializedName("carton_id")
    private int id;
    @SerializedName("flag_packed")
    private String packed;
    @SerializedName("carton_barcode")
    private String cartonBarcode;
    @SerializedName("po_number")
    private String poNo;
    @SerializedName("packinglist_id")
    private String packingListId;
    @SerializedName("pl_number")
    private String plNo;
    @SerializedName("carton_number")
    private String cartonNo;
    @SerializedName("gl_number")
    private String glNo;
    @SerializedName("buyer_name")
    private String buyer;
    private String season;
    private String content;
    @SerializedName("total_pcs")
    private String pcs;

    public int getId() {
        return id;
    }

    public String getPacked() {
        return packed;
    }

    public String getCartonBarcode() {
        return cartonBarcode;
    }

    public String getPoNo() {
        return poNo;
    }

    public String getPackingListId() {
        return packingListId;
    }

    public String getPlNo() {
        return plNo;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getSeason() {
        return season;
    }

    public String getContent() {
        return content;
    }

    public String getPcs() {
        return pcs;
    }

    public String getCartonNo() {
        return cartonNo;
    }

    public String getGlNo() {
        return glNo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPacked(String packed) {
        this.packed = packed;
    }

    public void setCartonBarcode(String cartonBarcode) {
        this.cartonBarcode = cartonBarcode;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public void setPackingListId(String packingListId) {
        this.packingListId = packingListId;
    }

    public void setPlNo(String plNo) {
        this.plNo = plNo;
    }

    public void setCartonNo(String cartonNo) {
        this.cartonNo = cartonNo;
    }

    public void setGlNo(String glNo) {
        this.glNo = glNo;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }
}
