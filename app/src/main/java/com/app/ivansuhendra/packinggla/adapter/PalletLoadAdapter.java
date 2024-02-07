package com.app.ivansuhendra.packinggla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.R;
import com.app.ivansuhendra.packinggla.model.Carton;
import com.app.ivansuhendra.packinggla.model.TransferNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PalletLoadAdapter extends RecyclerView.Adapter<PalletLoadAdapter.PalletLoadViewHolder> {
    private List<TransferNote> mItems;
    private Context mContext;
    private onItemClickListener mClicked;
    private String mPalletNo;
    private boolean[] checkedItems;

    public PalletLoadAdapter(Context mContext, List<TransferNote> mItems, String palletNo, onItemClickListener clicked) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mPalletNo = palletNo;
        this.mClicked = clicked;
        checkedItems = new boolean[mItems.size()];
        Arrays.fill(checkedItems, false);
    }

    public interface onItemClickListener {
        void onClick(View view, int position, TransferNote transferNote);
    }

    @NonNull
    @Override
    public PalletLoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pallet_load, parent, false);
        return new PalletLoadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletLoadViewHolder holder, int position) {
        TransferNote model = mItems.get(position);
        holder.tvSerialNumber.setText(String.valueOf(model.getSerialNumber()));
        holder.tvDate.setText(model.getReceiveAt() == null ? "-" : model.getReceiveAt());
        if (model.getCarton() != null) {
            int totalCartons = 0;
            int totalPcs = 0;
            for (Carton carton : model.getCarton()) {
                totalCartons++;
                totalPcs += carton.getPcs();
            }
            holder.tvCartons.setText(String.valueOf(totalCartons));
            holder.tvPcs.setText(String.valueOf(totalPcs));
        } else {
            holder.btnItemView.setVisibility(View.GONE);
            holder.tvCartons.setText("-");
            holder.tvPcs.setText("-");
        }

        // Set the state of the CheckBox based on the checkedItems array
        holder.checkBox.setChecked(checkedItems[position]);

        // Set an OnCheckedChangeListener for the CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the checked state in the array
            checkedItems[position] = isChecked;
        });

        holder.itemView.setOnClickListener(view -> {
            if (mClicked != null) {
                mClicked.onClick(view, position, model);
            }
        });
    }

    public void removeItem(int position) {
        mItems.remove(position);
        // Update the checkedItems array when an item is removed
        checkedItems = Arrays.copyOfRange(checkedItems, 0, mItems.size());
        notifyItemRemoved(position);
    }

    public List<TransferNote> getCheckedItems() {
        List<TransferNote> checkedItemList = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            if (checkedItems[i]) {
                checkedItemList.add(mItems.get(i));
            }
        }
        return checkedItemList;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class PalletLoadViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView tvSerialNumber;
        private TextView tvDate;
        private TextView tvCartons;
        private TextView tvPcs;
        private CardView btnItemView;


        public PalletLoadViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_transfer_note);
            tvSerialNumber = itemView.findViewById(R.id.tv_transfer_note_serial);
            tvDate = itemView.findViewById(R.id.tv_datetime_create_at);
            tvCartons = itemView.findViewById(R.id.tv_total_carton);
            tvPcs = itemView.findViewById(R.id.tv_total_pcs);
            btnItemView = itemView.findViewById(R.id.btn_item_transfer_note);

        }
    }
}