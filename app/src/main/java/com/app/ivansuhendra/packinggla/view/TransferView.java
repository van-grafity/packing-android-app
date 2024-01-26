package com.app.ivansuhendra.packinggla.view;

import com.app.ivansuhendra.packinggla.model.PalletTransfer;

import java.util.List;

public interface TransferView {
    void showProgress();

    void hideProgress();

    void showPalletTransfers(List<PalletTransfer> palletTransfers);

    void addPalletTransfers(List<PalletTransfer> palletTransfers);
}
