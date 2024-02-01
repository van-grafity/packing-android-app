package com.app.ivansuhendra.packinggla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.R;
import com.app.ivansuhendra.packinggla.model.TransferNote;

import java.util.List;

public class PalletLoadAdapter extends RecyclerView.Adapter<PalletLoadAdapter.PalletLoadViewHolder> {
    private List<TransferNote> mItems;
    private Context mContext;
    private PalletLoadAdapter.onItemClickListener mClicked;
    private String mPalletNo;

    public PalletLoadAdapter(Context mContext, List<TransferNote> mItems, String palletNo, PalletLoadAdapter.onItemClickListener clicked) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mPalletNo = palletNo;
        this.mClicked = clicked;
    }

    public interface onItemClickListener {
        void onClick(View view, int position, TransferNote transferNote);
    }

    @NonNull
    @Override
    public PalletLoadAdapter.PalletLoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pallet_load, parent, false);
        return new PalletLoadAdapter.PalletLoadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletLoadAdapter.PalletLoadViewHolder holder, int position) {
        TransferNote model = mItems.get(position);
        holder.tvSerialNumber.setText(String.valueOf(model.getSerialNumber()));
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size() == 0 ? 0 : mItems.size();
    }

    public static class PalletLoadViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNumber;

        public PalletLoadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNumber = itemView.findViewById(R.id.tv_transfer_note_serial);
        }
    }
}