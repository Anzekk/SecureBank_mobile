package com.secure_bank.bank.UI.Fragments.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.secure_bank.bank.Adapter.RecyclerViewSearch;
import com.secure_bank.bank.Listener.ActionHTMLCompleteListener;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.PortalSearch.PortalSearchApiCall;

import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static RecyclerViewSearch recyclerView_search;

    private static SwipeRefreshLayout swipeRefreshLayout;

    private static String filter = "";

    private static AppCompatEditText textSearch;
    private AppCompatImageButton search_btn;

    public static void refresh(boolean full) {
        PortalSearchApiCall.Execute(filter
                , new ActionHTMLCompleteListener() {
                    @Override
                    public void actionCompleted(List<String> data) {
                        Global.searchItems = data;
                        recyclerView_search = new RecyclerViewSearch(Global.context, Global.searchItems);
                        recyclerView.setAdapter(recyclerView_search);
                        recyclerView_search.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void actionFailed(String error) {
                        Log.e(Global.TAG, "Error: " + error);
                    }
                });
        if (full) {
            /*GetAvailableFundsApiCall.Execute(Global.person.UserName, new ActionIntegerCompleteListener() {
                @Override
                public void actionCompleted(Integer number) {
//                    if (MainNavigationDrawer.transactionMoney_tv != null)
//                        MainNavigationDrawer.transactionMoney_tv.setText(String.format("%d €", number));
                }

                @Override
                public void actionFailed(String error) {
                }
            });*/
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.fragment_search_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.fragment_search_refreshlayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        textSearch = view.findViewById(R.id.text_search);
        search_btn = view.findViewById(R.id.search_btn);

        refresh(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(true);
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter = Objects.requireNonNull(textSearch.getText()).toString();
                PortalSearchApiCall.Execute(filter, new ActionHTMLCompleteListener() {
                    @Override
                    public void actionCompleted(List<String> data) {
                        Global.searchItems = data;
                        recyclerView_search = new RecyclerViewSearch(Global.context, Global.searchItems);
                        recyclerView.setAdapter(recyclerView_search);
                        recyclerView_search.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void actionFailed(String error) {
                        Log.e(Global.TAG, "Error: " + error);
                    }
                });
            }
        });

        Global.context = getContext();

        return view;
    }
}