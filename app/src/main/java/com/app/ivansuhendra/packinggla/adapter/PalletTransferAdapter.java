package com.app.ivansuhendra.packinggla.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import org.w3c.dom.Text;

public class PalletTransferAdapter extends PagedListAdapter<PalletTransfer, PalletTransferAdapter.PalletTransferViewHolder> {

    private final Context mContext;
    private final itemAdapterOnClickHandler mClickHandler;

    public PalletTransferAdapter(Context context, itemAdapterOnClickHandler clickHandler) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PalletTransferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer, parent, false);
        return new PalletTransferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletTransferViewHolder holder, int position) {
        PalletTransfer palletTransfer = getItem(position);

        holder.bgStatus.setBackground(mContext.getDrawable(GlobalVars.provideStatus(palletTransfer.getStatus())));

        holder.tvStatusProgressLayer.setText(palletTransfer.getStatus());
        holder.tvTransactionNo.setText(palletTransfer.getTransactionNumber());
        holder.tvPalletNo.setText(palletTransfer.getPalletSerialNumber());
        holder.tvTotalCarton.setText(palletTransfer.getTotalCarton());
        holder.tvLocationFrom.setText(palletTransfer.getLocationFrom());

        if (palletTransfer != null) {
            // Bind your data to the ViewHolder here
            // For example, holder.bind(palletTransfer);

            // Set click listener
            holder.bgItemView.setOnClickListener(v -> mClickHandler.onClick(palletTransfer, v, position));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface itemAdapterOnClickHandler {
        void onClick(PalletTransfer palletTransfer, View view, int position);
    }

    static class PalletTransferViewHolder extends RecyclerView.ViewHolder {
        // Declare your views here
        private TextView tvStatusProgressLayer;
        private LinearLayout bgStatus;
        private CardView bgItemView;
        private TextView tvTransactionNo;
        private TextView tvPalletNo;
        private TextView tvTotalCarton;
        private TextView tvLocationFrom;

        PalletTransferViewHolder(View itemView) {
            super(itemView);
            // Initialize your views here
            tvStatusProgressLayer = itemView.findViewById(R.id.tvStatusProgressLayer);
            bgStatus = itemView.findViewById(R.id.bgStatus);
            bgItemView = itemView.findViewById(R.id.btnTransfer);
            tvTransactionNo = itemView.findViewById(R.id.tv_transaction_number);
            tvPalletNo = itemView.findViewById(R.id.tvPalletNo);
            tvTotalCarton = itemView.findViewById(R.id.tvTotalCarton);
            tvLocationFrom = itemView.findViewById(R.id.tvLocationFrom);
        }

        // You can have a bind method to bind data to views
        // For example:
        // void bind(PalletTransfer palletTransfer) {
        //     // Bind data to views
        // }
    }

    public static final DiffUtil.ItemCallback<PalletTransfer> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PalletTransfer>() {
                @Override
                public boolean areItemsTheSame(@NonNull PalletTransfer oldItem, @NonNull PalletTransfer newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull PalletTransfer oldItem, @NonNull PalletTransfer newItem) {
                    // Check if the items have the same content
                    return oldItem.equals(newItem);
                }
            };
}