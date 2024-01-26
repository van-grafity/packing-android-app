package com.app.ivansuhendra.packinggla.presenter;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;

import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.view.TransferView;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

public class TransferPresenterImpl implements TransferPresenter{
    private TransferView view;
    private TransferViewModel transferViewModel;
    private int currentPage = 1;
    private Activity activity;

    public TransferPresenterImpl(Activity activity, TransferView view, TransferViewModel transferViewModel) {
        this.view = view;
        this.transferViewModel = transferViewModel;
        this.activity = activity;
    }

    @Override
    public void loadData(String query) {
        view.showProgress();
        transferViewModel.getPalletTransferLiveData(currentPage, query).observe((LifecycleOwner) activity, apiResponse -> {
            view.showPalletTransfers(apiResponse.getData().getPalletTransfers());
        });
        view.hideProgress();
    }

    @Override
    public void loadMoreData(String query) {
        currentPage++;
        transferViewModel.getPalletTransferLiveData(currentPage, query).observe((LifecycleOwner) activity, apiResponse -> {
            view.addPalletTransfers(apiResponse.getData().getPalletTransfers());
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
