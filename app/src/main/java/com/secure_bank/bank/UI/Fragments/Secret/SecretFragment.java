package com.secure_bank.bank.UI.Fragments.Secret;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.secure_bank.bank.Adapter.RecyclerViewSecret;
import com.secure_bank.bank.Model.SudoItemModel;
import com.secure_bank.bank.R;
import com.secure_bank.bank.Util.AppChecker;
import com.secure_bank.bank.Util.Global;

import java.util.ArrayList;
import java.util.List;

public class SecretFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static RecyclerViewSecret recyclerView_secret;

    private static SwipeRefreshLayout swipeRefreshLayout;

    private List<SudoItemModel> sudoItems = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secret, container, false);
        Global.context = getContext();

        recyclerView = view.findViewById(R.id.fragment_secret_recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.fragment_secret_refreshlayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();

        return view;
    }


    private void refresh() {
        sudoItems.clear();
        boolean status;
        for (String packageName : AppChecker.applicationPackages) {

            status = AppChecker.InstalledOnDevice(Global.context, packageName);
            sudoItems.add(new SudoItemModel(packageName, status));
        }

        for (String path : AppChecker.applicationPaths) {
            status = AppChecker.FindPathOnDevice(path);
            sudoItems.add(new SudoItemModel(path, status));
        }

        status = AppChecker.CheckTestKeys();
        sudoItems.add(new SudoItemModel("Test keys", status));

        status = AppChecker.CheckDevKeys();
        sudoItems.add(new SudoItemModel("Dev keys", status));

        status = AppChecker.CheckRootProcess();
        sudoItems.add(new SudoItemModel("Sudo process test", status));

        recyclerView_secret = new RecyclerViewSecret(Global.context, sudoItems);
        recyclerView.setAdapter(recyclerView_secret);
        recyclerView_secret.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


}