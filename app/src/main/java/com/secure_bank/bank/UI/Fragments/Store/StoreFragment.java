package com.secure_bank.bank.UI.Fragments.Store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.secure_bank.bank.Adapter.RecyclerViewStore;
import com.secure_bank.bank.Listener.ActionGetStoreItemsCompleteListener;
import com.secure_bank.bank.Model.StoreItem;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Store.GetStoreItems.StoreGetStoreItemsApiCall;

import java.util.List;

public class StoreFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static RecyclerViewStore recyclerView_store;

    private static SwipeRefreshLayout swipeRefreshLayout;

    public static void refresh(boolean full) {
        StoreGetStoreItemsApiCall.Execute(new ActionGetStoreItemsCompleteListener() {
            @Override
            public void actionCompleted(List<StoreItem> storeItemList) {
                try {
                    Global.StoreItems = storeItemList;
                    recyclerView_store = new RecyclerViewStore(Global.context, Global.StoreItems);
                    recyclerView.setAdapter(recyclerView_store);
                } catch (Exception ex) {
                    Log.e(Global.TAG, ex.toString());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void actionFailed(String error) {
                Log.e(Global.TAG, "Error: " + error);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (full) {
           /* GetAvailableFundsApiCall.Execute(Global.person.UserName, new ActionIntegerCompleteListener() {
                @Override
                public void actionCompleted(Integer number) {
//                    MainNavigationDrawer.transactionMoney_tv.setText(String.format("%d €", number));
                }

                @Override
                public void actionFailed(String error) {
                    Log.e(Global.TAG, "Error: " + error);
                }
            });*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = view.findViewById(R.id.fragment_store_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.fragment_store_refreshlayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        if (Global.StoreItems.size() == 0) {
            StoreGetStoreItemsApiCall.Execute(new ActionGetStoreItemsCompleteListener() {
                @Override
                public void actionCompleted(List<StoreItem> storeItemList) {
                    Global.StoreItems = storeItemList;
                    recyclerView_store = new RecyclerViewStore(Global.context, Global.StoreItems);
                    recyclerView.setAdapter(recyclerView_store);
                    recyclerView_store.notifyDataSetChanged();
                }

                @Override
                public void actionFailed(String error) {
                    Log.e(Global.TAG, "Error: " + error);
                }
            });
        } else {
            refresh(true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(true);
            }
        });

        Global.context = getContext();

        return view;
    }
}