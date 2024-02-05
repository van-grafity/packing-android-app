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

public class TransferNoteAdapter extends RecyclerView.Adapter<TransferNoteAdapter.TransferViewHolder> {
    private List<TransferNote> mItems;
    private Context mContext;
    private TransferNoteAdapter.onItemClickListener mClicked;
    private String mPalletNo;

    public TransferNoteAdapter(Context mContext, List<TransferNote> mItems, String palletNo, TransferNoteAdapter.onItemClickListener clicked) {
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
    public TransferNoteAdapter.TransferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_transfer_note, parent, false);
        return new TransferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferNoteAdapter.TransferViewHolder holder, int position) {
        TransferNote model = mItems.get(position);
        holder.tvSerialNumber.setText(String.valueOf(model.getSerialNumber()));
        holder.tvDatetime.setText(model.getCreateAt() == null ? "-" : model.getCreateAt());
        holder.tvPalletNo.setText(mPalletNo == null ? "-" : mPalletNo);
        holder.tvTotalCarton.setText(String.valueOf(model.getTotalCarton() == null ? "-" : model.getTotalCarton()));
        holder.tvIssuedBy.setText(model.getIssuedBy() == null ? "-" : model.getIssuedBy());
        holder.tvPcs.setText(String.valueOf(model.getPcs() == 0 ? "-" : model.getPcs()));

        holder.btnItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClicked.onClick(v, holder.getAdapterPosition(), model);
            }
        });

    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size() == 0 ? 0 : mItems.size();
    }

    public static class TransferViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNumber;
        CardView btnItemView;
        TextView tvDatetime;
        TextView tvPalletNo;
        TextView tvTotalCarton;
        TextView tvIssuedBy;
        TextView tvPcs;

        public TransferViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNumber = itemView.findViewById(R.id.tv_transfer_note_serial);
            btnItemView = itemView.findViewById(R.id.btn_item_transfer_note);
            tvDatetime = itemView.findViewById(R.id.tv_datetime_create_at);
            tvPalletNo = itemView.findViewById(R.id.tv_pallet_no);
            tvTotalCarton = itemView.findViewById(R.id.tv_total_carton);
            tvIssuedBy = itemView.findViewById(R.id.tv_issued_by);
            tvPcs = itemView.findViewById(R.id.tv_total_pcs);
        }
    }
}