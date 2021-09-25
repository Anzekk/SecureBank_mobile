package com.secure_bank.bank.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.secure_bank.bank.R;

import java.util.List;

public class RecyclerViewSearch extends RecyclerView.Adapter<RecyclerViewSearch.ViewHolder> implements View.OnLongClickListener {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context activity;
    private List<String> mData;

    public RecyclerViewSearch(Context context, List<String> record) {
        this.mInflater = LayoutInflater.from(context);
        this.activity = context;
        this.mData = record;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_search, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = mData.get(position);

        holder.cardSearch_text.setText(data);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    String getItem(int id) {
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
        TextView cardSearch_text;


        ViewHolder(View itemView) {
            super(itemView);

            cardSearch_text = itemView.findViewById(R.id.cardSearch_text);

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
