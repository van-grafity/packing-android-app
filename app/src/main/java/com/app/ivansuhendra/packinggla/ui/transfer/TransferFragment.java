package com.app.ivansuhendra.packinggla.ui.transfer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";

    private TransferViewModel transferViewModel;
    private FragmentTransferBinding binding;
    private PalletTransferfAdapter palletTransferAdapter;
    private ProgressDialog progressDialog;
    private boolean isLoading = false;
    private int currentPage = 1;
    private Snackbar snackbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!isNetworkAvailable()) {
            // Tampilkan Snackbar dengan aksi refresh jika tidak ada koneksi
            showNoNetworkMessage();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = GlobalVars.pgDialog((MainActivity) getActivity());
                }
            });
            setupRecyclerView();

            // Initialize ViewModel and observe data changes
            transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
            loadInitialData("");

            // Implement scroll listener to load more data as user scrolls
            binding.rvPalletTransfer.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && !isLoading) {
                        // Reached end of list, load more data
                        loadMoreData("");
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

            transferViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    // Handle text changes if needed
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
                    loadInitialData(query);
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }
            });
        }
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

    private void loadInitialData(String query) {
        isLoading = true;
        transferViewModel.getPalletTransferLiveData(1, query).observe(getViewLifecycleOwner(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                isLoading = false;
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                palletTransferAdapter.setPalletTransfers(apiResponse.getData().getPalletTransfers());
            }
        });

    }

    private void loadMoreData(String query) {
        currentPage++;
        isLoading = true;
        transferViewModel.getPalletTransferLiveData(currentPage, query).observe(getViewLifecycleOwner(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                isLoading = false;
                palletTransferAdapter.addPalletTransfers(apiResponse.getData().getPalletTransfers());
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showNoNetworkMessage() {
        // Tambahkan logika untuk menampilkan pesan atau ambil tindakan yang sesuai
        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak ada koneksi internet. Coba lagi?", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Reload", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNetworkAvailable()) {
                            // Koneksi aktif, lakukan reload data
//                            setupRecyclerView();
                            loadInitialData("");
                        } else {
                            // Koneksi masih tidak aktif, tampilkan pesan atau ambil tindakan lain
                            showNoNetworkMessage();
                        }
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}