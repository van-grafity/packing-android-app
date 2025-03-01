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

    public LiveData<APIResponse> getPalletAvailability(String palletSerialNumber) {
        palletTransferResponseData = palletTransferRepository.getPalletAvailabilityResponse(palletSerialNumber);
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

    public LiveData<APIResponse> loginLiveData(String email, String password) {
        palletTransferResponseData = palletTransferRepository.loginResponse(email, password);
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

    public LiveData<APIResponse> completePreparationLiveData(int palletTransferId) {
        palletTransferResponseData = palletTransferRepository.completePreparationResponse(palletTransferId);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> searchCartonLiveData(String barcode) {
        palletTransferResponseData = palletTransferRepository.searchCartonResponse(barcode);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletReceiveLiveData(int page, String q) {
        palletTransferResponseData = palletTransferRepository.getPalletReceiveResponse(10, page, q);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletTransferLiveData(int page, String q) {
        palletTransferResponseData = palletTransferRepository.getPalletTransfersResponse(10, page, q);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getLocationLiveData() {
        palletTransferResponseData = palletTransferRepository.getLocationResponse();
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getRackLiveData(int page, String serialNo, String isEmpty) {
        palletTransferResponseData = palletTransferRepository.getRackResponse(30, page, serialNo, isEmpty);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> getPalletLoadData( String palletBarcode) {
        palletTransferResponseData = palletTransferRepository.searchPalletLoadResponse(palletBarcode);
        return palletTransferResponseData;
    }

    public LiveData<APIResponse> postPalletLoadData(Integer[] transferNoteIds) {
        palletTransferResponseData = palletTransferRepository.postPalletLoadResponse(transferNoteIds);
        return palletTransferResponseData;
    }
}