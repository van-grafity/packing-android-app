package com.app.ivansuhendra.packinggla.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ivansuhendra.packinggla.R;
import com.app.ivansuhendra.packinggla.model.Carton;

import java.util.ArrayList;
import java.util.List;

public class CartonAdapter extends RecyclerView.Adapter<CartonAdapter.CartonViewHolder> {
    private List<Carton> mItems;
    private Context mContext;
    private CartonAdapter.onItemClickListener mClicked;
    private boolean isClickable;

    public CartonAdapter(Context mContext, List<Carton> mItems, CartonAdapter.onItemClickListener clicked) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.mClicked = clicked;
        this.isClickable = false; // Atur ke false secara default
    }

    public void setClickable(boolean clickable) {
        this.isClickable = clickable;
        notifyDataSetChanged(); // Perbarui tampilan setelah perubahan status
    }

    public void setData(ArrayList<Carton> carton) {
        mItems.clear(); // Clear existing data
        mItems.addAll(carton); // Add new data
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    public void addItem(Carton carton) {
        mItems.add(carton);
    }

    public interface onItemClickListener {
        void onClick(View view, int position, Carton carton);
    }

    @NonNull
    @Override
    public CartonAdapter.CartonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_carton, parent, false);
        return new CartonAdapter.CartonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartonAdapter.CartonViewHolder holder, int position) {
        Carton model = mItems.get(position);
        holder.tvCartonNo.setText("Carton No. " + model.getCartonNo());
        holder.tvBuyer.setText(model.getBuyer());
        holder.tvPo.setText(model.getPoNo());
        holder.tvGl.setText(model.getGlNo());
        holder.tvContent.setText(model.getContent());
        holder.tvQty.setText(String.valueOf(model.getPcs()));
        holder.ivCarton.setAlpha(127);

//        holder.btnItemView.setEnabled(isClickable); // Atur keadaan klik berdasarkan status
        if (isClickable) {
            holder.btnItemView.setVisibility(View.VISIBLE);
        } else {
            // Tindakan jika tidak dapat diklik (opsional)
            holder.btnItemView.setVisibility(View.GONE);
        }

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
        TextView tvCartonNo;
        TextView tvBuyer;
        TextView tvPo;
        TextView tvGl;
        TextView tvContent;
        TextView tvQty;
        CardView btnItemView;
        ImageView ivCarton;

        public CartonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartonNo = itemView.findViewById(R.id.tv_carton_no);
            tvBuyer = itemView.findViewById(R.id.tv_buyer);
            tvPo = itemView.findViewById(R.id.tv_po);
            tvGl = itemView.findViewById(R.id.tv_gl);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvQty = itemView.findViewById(R.id.tv_qty);
            ivCarton = itemView.findViewById(R.id.iv_carton);

            btnItemView = itemView.findViewById(R.id.btnCarton);
        }
    }
}