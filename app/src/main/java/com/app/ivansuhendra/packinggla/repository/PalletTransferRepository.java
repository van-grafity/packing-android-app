package com.app.ivansuhendra.packinggla.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.net.API;
import com.app.ivansuhendra.packinggla.net.APICallback;
import com.app.ivansuhendra.packinggla.net.BadRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PalletTransferRepository {
    private static final String TAG = "PalletTransferRepositor";
    private Context mContext;

    public PalletTransferRepository(Context context) {
        this.mContext = context;
    }

    public LiveData<APIResponse> getPalletTransfersResponse(int limit, int page, String q) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getPalletTransfer(limit, page, q).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> savePalletTransferResponse(String palletSerialNumber, int locationFrom, int locationTo) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().savePalletTransferResponse(palletSerialNumber, locationFrom, locationTo).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> getPalletTransferDetailResponse(int id) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getPalletTransferDetail(id).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> getPalletTransferNoteResponse(int id) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getPalletTransferNote(id).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> loginResponse(String email, String password) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().loginResponse(email, password).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> getLocationResponse() {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getLocation().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> getRackResponse(int limit, int page, String serialNo) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getRack(limit, page, serialNo).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> updateTransferNoteResponse(int palletTransferId, int transferNoteId, Integer[] barcodeIds) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().updateTransferNote(palletTransferId, transferNoteId, barcodeIds).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                mutableLiveData.setValue(apiResponse);

            }

            @Override
            protected void onError(BadRequest error) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse>createPalletReceiveResponse(int palletTransferId, int rack, String receivedBy, String palletBarcode) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().createPalletReceive(palletTransferId, rack, receivedBy, palletBarcode).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                mutableLiveData.setValue(apiResponse);

            }

            @Override
            protected void onError(BadRequest error) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse>searchPalletReceiveResponse(String palletBarcode) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().searchPalletReceive(palletBarcode).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                mutableLiveData.setValue(apiResponse);

            }

            @Override
            protected void onError(BadRequest error) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> newTransferNoteResponse(int palletTransferId, Integer[] barcodeIds) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().newTransferNote(palletTransferId, barcodeIds).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                mutableLiveData.setValue(apiResponse);

            }

            @Override
            protected void onError(BadRequest error) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> completePreparationResponse(int palletTransferId) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().completePreparation(palletTransferId).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                mutableLiveData.setValue(apiResponse);

            }

            @Override
            protected void onError(BadRequest error) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> searchCartonResponse(String barcode) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().searchCarton(barcode).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<APIResponse> getPalletReceiveResponse(int limit, int page, String q) {
        final MutableLiveData<APIResponse> mutableLiveData = new MutableLiveData<>();
        API.service().getPalletReceive(limit, page, q).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }
}