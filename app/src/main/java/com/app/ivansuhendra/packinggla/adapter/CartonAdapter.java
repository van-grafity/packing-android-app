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

public class CartonAdapter extends RecyclerView.Adapter<CartonAdapter.CartonViewHolder> {
    private List<TransferNote> mItems;
    private Context mContext;
    private CartonAdapter.onItemClickListener mClicked;
    private String mPalletNo;

    public CartonAdapter(Context mContext, List<TransferNote> mItems, String palletNo, CartonAdapter.onItemClickListener clicked) {
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
    public CartonAdapter.CartonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_carton, parent, false);
        return new CartonAdapter.CartonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartonAdapter.CartonViewHolder holder, int position) {
        TransferNote model = mItems.get(position);
        holder.tvSerialNumber.setText(String.valueOf(model.getSerialNumber()));

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

    public static class CartonViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNumber;
        CardView btnItemView;

        public CartonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNumber = itemView.findViewById(R.id.tv_serial_number);
            btnItemView = itemView.findViewById(R.id.btn_add_carton);
        }
    }
}