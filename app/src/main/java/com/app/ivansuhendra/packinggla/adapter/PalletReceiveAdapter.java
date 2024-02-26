package com.app.ivansuhendra.packinggla.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.R;
import com.app.ivansuhendra.packinggla.model.PalletReceive;

import java.util.ArrayList;
import java.util.List;

public class PalletReceiveAdapter extends RecyclerView.Adapter<PalletReceiveAdapter.PalletReceiveViewHolder> {

    private final Context mContext;
    private final ArrayList<PalletReceive> mPalletTransfers;
    private final PalletReceiveAdapter.itemAdapterOnClickHandler mClickHandler;

    public PalletReceiveAdapter(Context context, ArrayList<PalletReceive> palletTransfers, PalletReceiveAdapter.itemAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mPalletTransfers = palletTransfers;
        this.mClickHandler = clickHandler;
    }

    public void setPalletReceives(List<PalletReceive> newPalletTransfers) {
        mPalletTransfers.clear();
        mPalletTransfers.addAll(newPalletTransfers);
        notifyDataSetChanged();
    }

    public void addPalletReceives(List<PalletReceive> newPalletTransfers) {
        int startPosition = mPalletTransfers.size();
        mPalletTransfers.addAll(newPalletTransfers);
        notifyItemRangeInserted(startPosition, newPalletTransfers.size());
    }

    @NonNull
    @Override
    public PalletReceiveAdapter.PalletReceiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive, parent, false);
        return new PalletReceiveAdapter.PalletReceiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletReceiveAdapter.PalletReceiveViewHolder holder, int position) {
        PalletReceive palletTransfer = mPalletTransfers.get(position);

        holder.bgStatus.setBackgroundColor(Color.parseColor("#" + palletTransfer.getColorCode()));

        holder.tvStatusProgressLayer.setText(palletTransfer.getStatus());
        holder.tvTransactionNo.setText(palletTransfer.getTransactionNumber());
        holder.tvPalletNo.setText(palletTransfer.getPalletSerialNumber());
        holder.tvTotalCarton.setText(palletTransfer.getTotalCarton());
        holder.tvLocationFrom.setText(palletTransfer.getLocationFrom());

        // Set click listener
        holder.bgItemView.setOnClickListener(v -> mClickHandler.onClick(palletTransfer, v, position));
    }

    @Override
    public int getItemCount() {
        return mPalletTransfers.size();
    }

    public interface itemAdapterOnClickHandler {
        void onClick(PalletReceive palletTransfer, View view, int position);
    }

    static class PalletReceiveViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatusProgressLayer;
        private LinearLayout bgStatus;
        private CardView bgItemView;
        private TextView tvTransactionNo;
        private TextView tvPalletNo;
        private TextView tvTotalCarton;
        private TextView tvLocationFrom;

        PalletReceiveViewHolder(View itemView) {
            super(itemView);
            tvStatusProgressLayer = itemView.findViewById(R.id.tvStatusProgressLayer);
            bgStatus = itemView.findViewById(R.id.bgStatus);
            bgItemView = itemView.findViewById(R.id.btnTransfer);
            tvTransactionNo = itemView.findViewById(R.id.tv_transaction_number);
            tvPalletNo = itemView.findViewById(R.id.tvPalletNo);
            tvTotalCarton = itemView.findViewById(R.id.tvTotalCarton);
            tvLocationFrom = itemView.findViewById(R.id.tvLocationFrom);
        }
    }
}
