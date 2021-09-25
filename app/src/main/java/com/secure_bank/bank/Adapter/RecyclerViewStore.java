package com.secure_bank.bank.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.StoreItem;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Store.BuyProduct.StoreBuyProductApiCall;

import java.util.List;

public class RecyclerViewStore extends RecyclerView.Adapter<RecyclerViewStore.ViewHolder> implements View.OnLongClickListener {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context activity;
    private List<StoreItem> mData;


    public RecyclerViewStore(Context context, List<StoreItem> record) {
        this.mInflater = LayoutInflater.from(context);
        this.activity = context;
        this.mData = record;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_store, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        StoreItem data = mData.get(position);

        holder.cardStore_name.setText(String.format("%s [%s]", data.Name, data.Installments));
        holder.cardStore_price.setText(String.format("%s €", data.Price));
        holder.cardStore_description.setText(String.format("%s", data.Description));

        setClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                StoreBuyProductApiCall.Execute(mData.get(position).Id, mData.get(position).Price, 1, new ActionCompleteListener() {
                                    @Override
                                    public void actionCompleted() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Global.context);
                                        builder.setMessage("Your purchase was successful!")
                                                .setNegativeButton("Close", null)
                                                .show();
                                    }

                                    @Override
                                    public void actionFailed(String error) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Global.context);
                                        builder.setMessage("Your purchase has failed!")
                                                .setNegativeButton("Close", null)
                                                .show();
                                    }
                                });
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(Global.context);
                builder.setMessage("Are you sure you want to buy: " + mData.get(position).Name + "?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    StoreItem getItem(int id) {
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
        TextView cardStore_name;
        TextView cardStore_price;
        TextView cardStore_description;

        ViewHolder(View itemView) {
            super(itemView);

            cardStore_name = itemView.findViewById(R.id.cardStore_name);
            cardStore_price = itemView.findViewById(R.id.cardStore_price);
            cardStore_description = itemView.findViewById(R.id.cardStore_description);

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
