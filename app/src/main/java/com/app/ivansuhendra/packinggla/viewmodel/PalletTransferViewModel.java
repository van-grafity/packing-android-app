package com.app.ivansuhendra.packinggla.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.net.PalletTransferDataSource;
import com.app.ivansuhendra.packinggla.net.PalletTransferDataSourceFactory;

public class PalletTransferViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private LiveData<PagedList<PalletTransfer>> pagedListLiveData;
    LiveData<PageKeyedDataSource<Integer, PalletTransfer>> liveDataSource;
    PalletTransferDataSourceFactory palletTransferDataSourceFactory;

    public PalletTransferViewModel() {
        Context context = new Activity();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        palletTransferDataSourceFactory = new PalletTransferDataSourceFactory(context);
    }

    public LiveData<PagedList<PalletTransfer>> getPagedListLiveData() {
        liveDataSource = palletTransferDataSourceFactory.getMachineLiveDataSource();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(PalletTransferDataSource.PAGE_SIZE)
                .build();

        pagedListLiveData = (new LivePagedListBuilder(palletTransferDataSourceFactory, config)).build();
        return pagedListLiveData;
    }
}