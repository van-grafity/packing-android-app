package com.app.ivansuhendra.packinggla.ui.receive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.app.ivansuhendra.packinggla.EditTransferNoteActivity;
import com.app.ivansuhendra.packinggla.MainActivity;
import com.app.ivansuhendra.packinggla.PalletReceiveDetailActivity;
import com.app.ivansuhendra.packinggla.ScanQrActivity;
import com.app.ivansuhendra.packinggla.adapter.PalletTransferfAdapter;
import com.app.ivansuhendra.packinggla.databinding.FragmentReceiveBinding;
import com.app.ivansuhendra.packinggla.model.APIResponse;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;
import com.app.ivansuhendra.packinggla.viewmodel.TransferViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ReceiveFragment extends Fragment {

    private FragmentReceiveBinding binding;
    private PalletTransferfAdapter palletTransferAdapter;
    private ProgressDialog progressDialog;
    private Snackbar snackbar;
    private TransferViewModel transferViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,

                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReceiveBinding.inflate(inflater, container, false);

        binding.btnItemReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PalletReceiveDetailActivity.class));
            }
        });


        View root = binding.getRoot();

        if (!isNetworkAvailable()) {
            // Tampilkan Snackbar dengan aksi refresh jika tidak ada koneksi
            showNoNetworkMessage();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    progressDialog = GlobalVars.pgDialog((MainActivity) getActivity());
                }
            });
            setupRecyclerView();

            binding.btnNewPalletReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScanQrActivity.class);
                    intent.putExtra(GlobalVars.PLT_CODE, "RECEIVE_CODE");
                    startActivity(intent);
                }
            });


            transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);

            loadDataTransferList();
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
        binding.rvPalletReceive.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
    }

    private void filterData(String query) {
        // Implement filtering logic here based on the search query
        // You might want to filter the data in your ViewModel and update the RecyclerView accordingly
//        palletTransferViewModel.setSearchQuery(query);
    }

    private void loadDataTransferList() {
//        snackbar.dismiss(); // Tutup Snackbar setelah berhasil reload

        transferViewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        transferViewModel.getPalletReceiveLiveData(100, 1, "").observe(getViewLifecycleOwner(), new Observer<APIResponse>() {
            @Override
            public void onChanged(APIResponse apiResponse) {
                palletTransferAdapter = new PalletTransferfAdapter(getActivity(), apiResponse.getData().getPalletReceive(), new PalletTransferfAdapter.itemAdapterOnClickHandler() {
                    @Override
                    public void onClick(PalletTransfer palletTransfer, View view, int position) {
                        Toast.makeText(getActivity(), palletTransfer.getTransactionNumber(), Toast.LENGTH_SHORT).show();
                    }
                });
                binding.rvPalletReceive.setAdapter(palletTransferAdapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}