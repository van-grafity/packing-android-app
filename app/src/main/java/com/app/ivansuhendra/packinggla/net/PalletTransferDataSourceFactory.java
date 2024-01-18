package com.app.ivansuhendra.packinggla.net;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.app.ivansuhendra.packinggla.model.PalletTransfer;

public class PalletTransferDataSourceFactory extends DataSource.Factory<Integer, PalletTransfer> {
    private Context mContext;
    private MutableLiveData<PageKeyedDataSource<Integer, PalletTransfer>> mMachineLiveData = new MutableLiveData<>();

    public PalletTransferDataSourceFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public DataSource<Integer, PalletTransfer> create() {
        PalletTransferDataSource palletTransferDataSource = new PalletTransferDataSource(mContext);
        mMachineLiveData.postValue(palletTransferDataSource);
        return palletTransferDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, PalletTransfer>> getMachineLiveDataSource() {
        return mMachineLiveData;
    }
}