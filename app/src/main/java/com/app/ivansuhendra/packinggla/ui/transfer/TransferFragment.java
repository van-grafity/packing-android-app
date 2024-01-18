package com.app.ivansuhendra.packinggla.ui.transfer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.ivansuhendra.packinggla.NewPalletTransferActivity;
import com.app.ivansuhendra.packinggla.PalletTransferDetailActivity;
import com.app.ivansuhendra.packinggla.ScanQrActivity;
import com.app.ivansuhendra.packinggla.adapter.PalletTransferAdapter;
import com.app.ivansuhendra.packinggla.databinding.FragmentTransferBinding;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.PalletTransferViewModel;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";

    private TransferViewModel transferViewModel;
    private FragmentTransferBinding binding;
    private PalletTransferViewModel palletTransferViewModel;
    private PalletTransferAdapter palletTransferAdapter;
    private Snackbar snackbar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!isNetworkAvailable()) {
            // Tampilkan Snackbar dengan aksi refresh jika tidak ada koneksi
            showNoNetworkMessage();
        } else {
            setupRecyclerView();

            binding.btnNewPalletTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                    intent.putExtra(GlobalVars.PLT_CODE, "PLT_CODE");
                    startActivity(intent);
                }
            });

            transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

            loadDataTransferList();

            transferViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    // Handle text changes if needed
                }
            });

            binding.editQuery.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String query = editable.toString().trim();
                    filterData(query);
                }
            });
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showNoNetworkMessage() {
        // Tambahkan logika untuk menampilkan pesan atau ambil tindakan yang sesuai
        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Tidak ada koneksi internet. Coba lagi?", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Reload", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNetworkAvailable()) {
                            // Koneksi aktif, lakukan reload data
                            setupRecyclerView();
                            loadDataTransferList();
                        } else {
                            // Koneksi masih tidak aktif, tampilkan pesan atau ambil tindakan lain
                            showNoNetworkMessage();
                        }
                    }
                })
                .show();
    }

    private void setupRecyclerView() {
        binding.rvPalletTransfer.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        palletTransferAdapter = new PalletTransferAdapter(getActivity(), new PalletTransferAdapter.itemAdapterOnClickHandler() {
            @Override
            public void onClick(PalletTransfer plTransfer, View view, int position) {
                Intent intent = new Intent(getActivity(), PalletTransferDetailActivity.class);
                intent.putExtra(GlobalVars.PALLET_TRANSFER_LIST, plTransfer);
                startActivity(intent);
            }
        });
        binding.rvPalletTransfer.setAdapter(palletTransferAdapter);
    }

    private void filterData(String query) {
        // Implement filtering logic here based on the search query
        // You might want to filter the data in your ViewModel and update the RecyclerView accordingly
//        palletTransferViewModel.setSearchQuery(query);
    }

    private void loadDataTransferList() {
//        snackbar.dismiss(); // Tutup Snackbar setelah berhasil reload
        palletTransferViewModel = new ViewModelProvider(this).get(PalletTransferViewModel.class);
        palletTransferViewModel.getPagedListLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<PalletTransfer>>() {
            @Override
            public void onChanged(PagedList<PalletTransfer> palletTransfers) {
                palletTransferAdapter.submitList(palletTransfers);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}