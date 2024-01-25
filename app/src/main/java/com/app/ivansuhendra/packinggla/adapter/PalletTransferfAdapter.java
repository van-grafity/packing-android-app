package com.app.ivansuhendra.packinggla.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.R;
import com.app.ivansuhendra.packinggla.model.PalletTransfer;
import com.app.ivansuhendra.packinggla.utils.GlobalVars;

import java.util.ArrayList;
import java.util.List;

public class PalletTransferfAdapter extends RecyclerView.Adapter<PalletTransferfAdapter.PalletTransferViewHolder> {

    private final Context mContext;
    private final ArrayList<PalletTransfer> mPalletTransfers;
    private final itemAdapterOnClickHandler mClickHandler;

    public PalletTransferfAdapter(Context context, ArrayList<PalletTransfer> palletTransfers, itemAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mPalletTransfers = palletTransfers;
        this.mClickHandler = clickHandler;
    }

    public void setPalletTransfers(List<PalletTransfer> newPalletTransfers) {
        mPalletTransfers.clear();
        mPalletTransfers.addAll(newPalletTransfers);
        notifyDataSetChanged();
    }

    public void addPalletTransfers(List<PalletTransfer> newPalletTransfers) {
        int startPosition = mPalletTransfers.size();
        mPalletTransfers.addAll(newPalletTransfers);
        notifyItemRangeInserted(startPosition, newPalletTransfers.size());
    }

    @NonNull
    @Override
    public PalletTransferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        return new PalletTransferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletTransferViewHolder holder, int position) {
        PalletTransfer palletTransfer = mPalletTransfers.get(position);

        holder.bgStatus.setBackgroundColor(Color.parseColor("#"+palletTransfer.getColorCode()));

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
        void onClick(PalletTransfer palletTransfer, View view, int position);
    }

    static class PalletTransferViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatusProgressLayer;
        private LinearLayout bgStatus;
        private CardView bgItemView;
        private TextView tvTransactionNo;
        private TextView tvPalletNo;
        private TextView tvTotalCarton;
        private TextView tvLocationFrom;

        PalletTransferViewHolder(View itemView) {
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
