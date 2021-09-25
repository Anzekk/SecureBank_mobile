package com.secure_bank.bank.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.secure_bank.bank.Model.SudoItemModel;
import com.secure_bank.bank.R;

import java.util.List;

public class RecyclerViewSecret extends RecyclerView.Adapter<RecyclerViewSecret.ViewHolder> implements View.OnLongClickListener {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<SudoItemModel> mData;

    public RecyclerViewSecret(Context context, List<SudoItemModel> record) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = record;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_secret, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SudoItemModel data = mData.get(position);

        holder.cardSecret_tv.setText(data.name);

        if (data.isUsed)
            holder.cardSecret_iv.setImageResource(R.drawable.found);
        else
            holder.cardSecret_iv.setImageResource(R.drawable.not_found);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    SudoItemModel getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        AppCompatImageView cardSecret_iv;
        TextView cardSecret_tv;


        ViewHolder(View itemView) {
            super(itemView);

            cardSecret_iv = itemView.findViewById(R.id.cardSecret_iv);
            cardSecret_tv = itemView.findViewById(R.id.cardSecret_tv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null) mClickListener.onItemLongClick(view, getAdapterPosition());
            return true;
        }

    }
}
