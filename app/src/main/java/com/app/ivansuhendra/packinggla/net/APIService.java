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

    @FormUrlEncoded
    @POST("pallet-transfer")
    Call<APIResponse> savePalletTransferResponse(@Field("pallet_serial_number") String palletSerialNumber,@Field("location_from") int locationFrom,@Field("location_to") int locationTo);

    @GET("pallet-transfer/" + "{id}")
    Call<APIResponse> getPalletTransferDetail(@Path("id") int id);

    @GET("pallet-transfer/"+ "transfer-note-edit/" + "{id}")
    Call<APIResponse> getPalletTransferNote(@Path("id") int id);

    @FormUrlEncoded
    @PUT("pallet-transfer/" + "transfer-note-update")
    Call<APIResponse> updateTransferNote(@Field("pallet_transfer_id") int palletTransferId, @Field("transfer_note_id") int transferNoteId, @Field("carton_barcode_id[]") Integer[] barcodeIds);
}
