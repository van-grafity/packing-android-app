package com.app.ivansuhendra.packinggla.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> navigateToTransfer = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateToTransfer() {
        return navigateToTransfer;
    }

    public void setNavigateToTransfer(boolean navigate) {
        navigateToTransfer.setValue(navigate);
    }
}
