package com.app.ivansuhendra.packinggla.ui.receive;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.ivansuhendra.packinggla.NewPalletTransferActivity;
import com.app.ivansuhendra.packinggla.PalletReceiveDetailActivity;
import com.app.ivansuhendra.packinggla.PalletTransferDetailActivity;
import com.app.ivansuhendra.packinggla.ScanQrActivity;
import com.app.ivansuhendra.packinggla.databinding.FragmentReceiveBinding;
import com.app.ivansuhendra.packinggla.viewmodel.ReceiveViewModel;

public class ReceiveFragment extends Fragment {

    private ReceiveViewModel receiveViewModel;
    private FragmentReceiveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             
                             ViewGroup container, Bundle savedInstanceState) {
        receiveViewModel =
                new ViewModelProvider(this).get(ReceiveViewModel.class);

        binding = FragmentReceiveBinding.inflate(inflater, container, false);

        binding.btnItemReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PalletReceiveDetailActivity.class));
            }
        });

        binding.btnNewPalletReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanQrActivity.class));
            }
        });
        View root = binding.getRoot();

        receiveViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}