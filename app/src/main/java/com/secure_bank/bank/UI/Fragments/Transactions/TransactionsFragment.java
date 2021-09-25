package com.secure_bank.bank.UI.Fragments.Transactions;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.secure_bank.bank.Adapter.RecyclerViewTransactions;
import com.secure_bank.bank.Listener.ActionGetTransactionsCompleteListener;
import com.secure_bank.bank.Listener.ActionIntegerCompleteListener;
import com.secure_bank.bank.Model.TransactionResp;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Transaction.GetTransactions.GetTransactionsApiCall;
import com.secure_bank.bank.WebServices.User.GetAvaliableFunds.GetAvailableFundsApiCall;

import java.util.List;
import java.util.Objects;

public class TransactionsFragment extends Fragment {

    public static TextView transactionMoney_tv;
    private static RecyclerView recyclerView;
    private static RecyclerViewTransactions recyclerView_transactions;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static AppCompatEditText transactionSearch;
    private static String filter = "";
    public TextView transactionAccount_tv;

    Button animateSearch_btn;
    LinearLayout searchingBar_ll;
    private AppCompatImageButton searching_btn;

    public static void refresh(boolean full) {
        GetTransactionsApiCall.Execute(0, 100, filter, new ActionGetTransactionsCompleteListener() {
            @Override
            public void actionCompleted(List<TransactionResp> record) {
                Global.transactions = record;
                recyclerView_transactions = new RecyclerViewTransactions(Global.context, Global.transactions);
                recyclerView.setAdapter(recyclerView_transactions);
                recyclerView_transactions.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void actionFailed(String error) {
                Log.e(Global.TAG, "Error: " + error);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (full) {
            GetAvailableFundsApiCall.Execute(Global.person.UserName, new ActionIntegerCompleteListener() {
                @Override
                public void actionCompleted(Integer number) {
                    if (TransactionsFragment.transactionMoney_tv != null) {
                        TransactionsFragment.transactionMoney_tv.setText(String.format("%d €", number));
                    }
                }

                @Override
                public void actionFailed(String error) {
                    Log.e(Global.TAG, "Error: " + error);
                }
            });
        }
    }

    public static Fragment newInstance() {
        TransactionsFragment fragment = new TransactionsFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        transactionSearch = view.findViewById(R.id.transactionSearch);
        searching_btn = view.findViewById(R.id.searching_btn);

        searching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = Objects.requireNonNull(transactionSearch.getText()).toString();
                ifFilterBtnColorChange();
                GetTransactionsApiCall.Execute(0, 100, filter, new ActionGetTransactionsCompleteListener() {
                    @Override
                    public void actionCompleted(List<TransactionResp> record) {
                        Global.transactions = record;
                        recyclerView_transactions = new RecyclerViewTransactions(Global.context, Global.transactions);
                        recyclerView.setAdapter(recyclerView_transactions);
                        recyclerView_transactions.notifyDataSetChanged();
                    }

                    @Override
                    public void actionFailed(String error) {
                        Log.e(Global.TAG, "Error: " + error);
                    }
                });
            }
        });

        transactionMoney_tv = view.findViewById(R.id.transactionMoney_tv);
        transactionAccount_tv = view.findViewById(R.id.transactionAccount_tv);
        animateSearch_btn = view.findViewById(R.id.animateSearch_btn);
        searchingBar_ll = view.findViewById(R.id.searchingBar_ll);

        ifFilterBtnColorChange();
        animateSearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchingBar_ll.getVisibility() == View.GONE) {
                    animateSearch_btn.setBackground(ContextCompat.getDrawable(Global.context, R.drawable.ic_baseline_arrow_drop_up_24));
                    searchingBar_ll.setVisibility(View.VISIBLE);
                    ifFilterBtnColorChange();
                } else {
                    searchingBar_ll.setVisibility(View.GONE);
                    animateSearch_btn.setBackground(ContextCompat.getDrawable(Global.context, R.drawable.ic_baseline_arrow_drop_down_24));
                    ifFilterBtnColorChange();
                }

            }
        });

        transactionAccount_tv.setText(Global.person.UserName);

        recyclerView = view.findViewById(R.id.fragment_transactions_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.fragment_transaction_refreshlayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        refresh(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(true);
            }
        });

        Global.context = getContext();

        return view;
    }

    private void ifFilterBtnColorChange() {
        if (filter.length() == 0) {
            animateSearch_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Global.context, R.color.grayShade)));
        } else {
            animateSearch_btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(Global.context, R.color.colorBlack)));
        }
    }
}