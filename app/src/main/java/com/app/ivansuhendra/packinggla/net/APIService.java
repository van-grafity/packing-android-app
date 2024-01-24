package com.app.ivansuhendra.packinggla.net;

import com.app.ivansuhendra.packinggla.model.APIResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("pallet-transfer")
    Call<APIResponse> getPalletTransfer(@Query("limit") int limit, @Query("page") int page);

    @GET("pallet-receive")
    Call<APIResponse> getPalletReceive(@Query("limit") int limit, @Query("page") int page);

    @FormUrlEncoded
    @POST("pallet-receive")
    Call<APIResponse> createPalletReceive(@Field("pallet_transfer_id") int palletTransferId, @Field("rack") int rack, @Field("received_by") String receivedBy, @Field("pallet_barcode") String palletBarcode);

    @GET("pallet-receive/" + "search-pallet")
    Call<APIResponse> searchPalletReceive(@Query("pallet_barcode") String palletBarcode);

    @FormUrlEncoded
    @POST("pallet-transfer")
    Call<APIResponse> savePalletTransferResponse(@Field("pallet_serial_number") String palletSerialNumber,@Field("location_from") int locationFrom,@Field("location_to") int locationTo);

    @FormUrlEncoded
    @POST("login")
    Call<APIResponse> loginResponse(@Field("email") String email,@Field("password") String password);

    @GET("pallet-transfer/" + "{id}")
    Call<APIResponse> getPalletTransferDetail(@Path("id") int id);

    @GET("pallet-transfer/"+ "transfer-note-edit/" + "{id}")
    Call<APIResponse> getPalletTransferNote(@Path("id") int id);

    @FormUrlEncoded
    @PUT("pallet-transfer/" + "transfer-note-update")
    Call<APIResponse> updateTransferNote(@Field("pallet_transfer_id") int palletTransferId, @Field("transfer_note_id") int transferNoteId, @Field("carton_barcode_id[]") Integer[] barcodeIds);

    @FormUrlEncoded
    @POST("pallet-transfer/" + "transfer-note-store")
    Call<APIResponse> newTransferNote(@Field("pallet_transfer_id") int palletTransferId, @Field("carton_barcode_id[]") Integer[] barcodeIds);

    @GET("pallet-transfer/" + "search-carton")
    Call<APIResponse> searchCarton(@Query("carton_barcode") String barcode);

    @GET("location")
    Call<APIResponse> getLocation();

    @GET("rack")
    Call<APIResponse> getRack(@Query("limit") int limit, @Query("page") int page, @Query("serial_number") String serialNo);

    @GET("pallet-loading/" + "search-pallet")
    Call<APIResponse> searchPalletLoad(@Query("pallet_barcode") String palletBarcode);

    @FormUrlEncoded
    @POST("pallet-loading")
    Call<APIResponse> postPalletLoad(@Field("transfer_note_id[]") Integer[] transferNoteIds);
}
