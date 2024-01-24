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
import com.app.ivansuhendra.packinggla.model.Carton;
import com.app.ivansuhendra.packinggla.model.Rack;

import java.util.ArrayList;
import java.util.List;

public class RackAdapter extends RecyclerView.Adapter<RackAdapter.RackViewHolder> {
    private List<Rack> mItems;
    private Context mContext;
    private RackAdapter.onItemClickListener mClicked;

    public RackAdapter(Context mContext, List<Rack> mItems, RackAdapter.onItemClickListener clicked) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mClicked = clicked;
    }

    public interface onItemClickListener {
        void onClick(View view, int position, Rack carton);
    }

    @NonNull
    @Override
    public RackAdapter.RackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rack, parent, false);
        return new RackAdapter.RackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RackAdapter.RackViewHolder holder, int position) {
        Rack model = mItems.get(position);
        holder.tvSerialNumber.setText(model.getName());

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

    public void addData(List<Rack> newData) {
        mItems.clear();  // Clear existing data before adding new data
        mItems.addAll(newData);
        notifyDataSetChanged();
//        int startPosition = mItems.size();
//        mItems.addAll(newData);
//        notifyItemRangeInserted(startPosition, newData.size());
    }

    @Override
    public int getItemCount() {
        return mItems.size() == 0 ? 0 : mItems.size();
    }

    public static class RackViewHolder extends RecyclerView.ViewHolder {
        TextView tvSerialNumber;
        CardView btnItemView;

        public RackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSerialNumber = itemView.findViewById(R.id.tvName);
            btnItemView = itemView.findViewById(R.id.btnItemView);
        }
    }
}