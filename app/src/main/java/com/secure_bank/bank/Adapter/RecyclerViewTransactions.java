package com.secure_bank.bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.secure_bank.bank.Model.TransactionResp;
import com.secure_bank.bank.R;
import com.secure_bank.bank.UI.Activity.DetailTransactionActivity;
import com.secure_bank.bank.Util.DateTimeHelper;
import com.secure_bank.bank.Util.Global;

import java.util.List;

public class RecyclerViewTransactions extends RecyclerView.Adapter<RecyclerViewTransactions.ViewHolder> implements View.OnLongClickListener {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context activity;
    private List<TransactionResp> mData;

    public RecyclerViewTransactions(Context context, List<TransactionResp> data) {
        this.mInflater = LayoutInflater.from(context);
        this.activity = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_transaction, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionResp data = mData.get(position);
        if (!data.senderId.equals(Global.person.UserName)) {
            holder.transaction_money_tv.setTextColor(ContextCompat.getColor(Global.context, R.color.positiveAmount));
        } else {
            holder.transaction_money_tv.setTextColor(ContextCompat.getColor(Global.context, R.color.negativeAmount));
        }
        holder.transaction_money_tv.setText(String.format("%.2f €", data.amount));

        holder.transaction_date_tv.setText(DateTimeHelper.convertStringToNewFormatString(data.dateTime, "MM/dd/yyyy", "dd. MM. yyyy"));
        holder.transaction_reason_tv.setText(data.reason);

        setClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Global.context, DetailTransactionActivity.class);
                intent.putExtra("TRANSACTION_ID", mData.get(position).id);
                Global.context.startActivity(intent);
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

    TransactionResp getItem(int id) {
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
        TextView transaction_reason_tv;
        TextView transaction_money_tv;
        TextView transaction_date_tv;


        ViewHolder(View itemView) {
            super(itemView);

            transaction_reason_tv = itemView.findViewById(R.id.transaction_reason_tv);
            transaction_money_tv = itemView.findViewById(R.id.transaction_money_tv);
            transaction_date_tv = itemView.findViewById(R.id.transaction_date_tv);

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
