package com.app.ivansuhendra.packinggla.presenter;

public interface TransferPresenter {
    void loadData(String query);

    void loadMoreData(String query);

    void onDestroy();
}
