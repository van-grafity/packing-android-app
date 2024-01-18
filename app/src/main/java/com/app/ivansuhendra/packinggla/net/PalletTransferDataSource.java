package com.app.ivansuhendra.packinggla.net;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;

import java.util.ArrayList;

public class PalletTransferDataSource extends PageKeyedDataSource<Integer, PalletTransfer> {
    private static final String TAG = "PalletTransferDataSource";

    private Context mContext;

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;

    public PalletTransferDataSource(Context context) {
        this.mContext = context;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, PalletTransfer> callback) {
        fetchData(FIRST_PAGE, params.requestedLoadSize, new DataCallback() {
            @Override
            public void onDataLoaded(ArrayList<PalletTransfer> data, Integer adjacentKey) {
                callback.onResult(data, null, FIRST_PAGE + 1);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, PalletTransfer> callback) {
        fetchData(params.key, PAGE_SIZE, new DataCallback() {
            @Override
            public void onDataLoaded(ArrayList<PalletTransfer> data, Integer adjacentKey) {
                if (data.size() > 0) {
                    callback.onResult(data, adjacentKey);
                }
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, PalletTransfer> callback) {
        fetchData(params.key, params.requestedLoadSize, new DataCallback() {
            @Override
            public void onDataLoaded(ArrayList<PalletTransfer> data, Integer adjacentKey) {
                if (data.size() > 0) {
                    Integer key = data.size() == PAGE_SIZE ? params.key + 1 : null;
                    callback.onResult(data, key);
                }
            }
        });
    }

    private void fetchData(int page, int pageSize, final DataCallback dataCallback) {
        API.service().getPalletTransfer(pageSize, page).enqueue(new APICallback<APIResponse>(mContext) {
            @Override
            protected void onSuccess(APIResponse apiResponse) {
                ArrayList<PalletTransfer> palletTransfers = apiResponse.getData().getPalletTransfers();
                Integer adjacentKey = (page > 1) ? page - 1 : null;
                dataCallback.onDataLoaded(palletTransfers, adjacentKey);
            }

            @Override
            protected void onError(BadRequest error) {
//                Log.d( "onError: " + error.errors);
            }
        });
    }

    private interface DataCallback {
        void onDataLoaded(ArrayList<PalletTransfer> data, Integer adjacentKey);
    }
}