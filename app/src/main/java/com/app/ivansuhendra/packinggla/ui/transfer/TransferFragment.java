package com.app.ivansuhendra.packinggla.ui.transfer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.MainActivity;
import com.app.ivansuhendra.packinggla.PalletTransferDetailActivity;
import com.app.ivansuhendra.packinggla.ScanQrActivity;
import com.app.ivansuhendra.packinggla.adapter.PalletTransferfAdapter;
import com.app.ivansuhendra.packinggla.databinding.FragmentTransferBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.presenter.TransferPresenter;
import com.app.ivansuhendra.packinggla.presenter.TransferPresenterImpl;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.view.TransferView;
import com.app.ivansuhendra.packinggla.view.TransferViewImpl;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";

    private TransferViewModel transferViewModel;
    private FragmentTransferBinding binding;
    private PalletTransferfAdapter palletTransferAdapter;
    private ProgressDialog progressDialog;
    private boolean isLoading = false;
    private TransferPresenter presenter;
    private TransferView transferView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();

        transferView = new TransferViewImpl(getActivity(), palletTransferAdapter);
        presenter = new TransferPresenterImpl(getActivity(), transferView, new ViewModelProvider(this).get(TransferViewModel.class));
        presenter.loadData("");

        // Implement scroll listener to load more data as user scrolls
        binding.rvPalletTransfer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    // Reached end of list, load more data
                    presenter.loadMoreData(binding.etSearch.getText().toString());
                }
            }
        });

        binding.btnNewPalletTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                intent.putExtra(GlobalVars.PLT_CODE, "PLT_CODE");
                startActivity(intent);
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
//                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                // Check if the query is not empty and if the user pressed enter
                presenter.loadData(query);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupRecyclerView() {
        palletTransferAdapter = new PalletTransferfAdapter(getActivity(), new ArrayList<>(), new PalletTransferfAdapter.itemAdapterOnClickHandler() {
            @Override
            public void onClick(PalletTransfer palletTransfer, View view, int position) {
                Intent intent = new Intent(getActivity(), PalletTransferDetailActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, palletTransfer);
                startActivity(intent);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvPalletTransfer.setLayoutManager(layoutManager);
        binding.rvPalletTransfer.setAdapter(palletTransferAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}