package com.app.ivansuhendra.packinggla.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.repository.PalletTransferRepository;

public class TransferViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private PalletTransferRepository palletTransferRepository;
    private LiveData<APIResponse> palletTransferResponseData;

    public TransferViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is transfer fragment");
        Context context = new Activity();
        palletTransferRepository = new PalletTransferRepository(context);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<APIResponse> savePalletTransferLiveData(String palletSerialNumber, int locationFrom, int locationTo) {
        palletTransferResponseData = palletTransferRepository.savePalletTransferResponse(palletSerialNumber, locationFrom, locationTo);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletTransferDetailLiveData(int id) {
        palletTransferResponseData = palletTransferRepository.getPalletTransferDetailResponse(id);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletTransferNoteLiveData(int id) {
        palletTransferResponseData = palletTransferRepository.getPalletTransferNoteResponse(id);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> updateTransferNoteLiveData(int palletTransferId, int transferNoteId, Integer[] barcodeIds) {
        palletTransferResponseData = palletTransferRepository.updateTransferNoteResponse(palletTransferId, transferNoteId, barcodeIds);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> createPalletReceiveLiveData(int palletTransferId, int rack, String receivedBy, String palletBarcode) {
        palletTransferResponseData = palletTransferRepository.createPalletReceiveResponse(palletTransferId, rack, receivedBy, palletBarcode);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> searchPalletReceiveLiveData(String palletBarcode) {
        palletTransferResponseData = palletTransferRepository.searchPalletReceiveResponse(palletBarcode);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> newTransferNoteLiveData(int palletTransferId, Integer[] barcodeIds) {
        palletTransferResponseData = palletTransferRepository.newTransferNoteResponse(palletTransferId, barcodeIds);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> searchCartonLiveData(String barcode) {
        palletTransferResponseData = palletTransferRepository.searchCartonResponse(barcode);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletReceiveLiveData(int limit, int page) {
        palletTransferResponseData = palletTransferRepository.getPalletReceiveResponse(limit, page);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletTransferLiveData(int limit, int page) {
        palletTransferResponseData = palletTransferRepository.getPalletTransfersResponse(limit, page);
        return palletTransferResponseData;
    }
}