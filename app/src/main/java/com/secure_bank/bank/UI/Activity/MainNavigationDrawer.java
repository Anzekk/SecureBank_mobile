package com.secure_bank.bank.UI.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.R;
import com.secure_bank.bank.UI.Fragments.Search.SearchFragment;
import com.secure_bank.bank.UI.Fragments.Secret.SecretFragment;
import com.secure_bank.bank.UI.Fragments.Store.StoreFragment;
import com.secure_bank.bank.UI.Fragments.Transactions.NewTransactionsFragment;
import com.secure_bank.bank.UI.Fragments.Transactions.TransactionsFragment;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.WebServices.Auth.Logout.AuthLogoutApiCall;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainNavigationDrawer extends AppCompatActivity {

    static final Fragment transactions = new TransactionsFragment();
    static DrawerLayout drawer;
    static FragmentManager fragmentManager;
    static Fragment active = transactions;
    final Fragment createTransactions = new NewTransactionsFragment();
    final Fragment store = new StoreFragment();
    final Fragment search = new SearchFragment();
    final Fragment secret = new SecretFragment();
    CircleImageView user_iv;
    TextView user_tv;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavController navController = Navigation.findNavController(this, R.id.content_main_frag);

        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);


        if (Global.rootUser) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer_with_secret);

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_transaction, R.id.navigation_addTransaction, R.id.navigation_store, R.id.navigation_search, R.id.navigation_secret)
                    .setDrawerLayout(drawer)
                    .build();
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_transaction, R.id.navigation_addTransaction, R.id.navigation_store, R.id.navigation_search)
                    .setDrawerLayout(drawer)
                    .build();
        }
        fragmentManager = getSupportFragmentManager();


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction;
                switch (item.getItemId()) {
                    case R.id.navigation_transaction:
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_main_frag, transactions);
                        transaction.commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_transactions);
                        break;
                    case R.id.navigation_store:
                        active = transactions;
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_main_frag, store);
                        transaction.commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_store);
                        break;
                    case R.id.navigation_search:
                        active = store;
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_main_frag, search);
                        transaction.commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_search);
                        break;
                    case R.id.navigation_secret:
                        active = search;
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_main_frag, secret);
                        transaction.commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.title_secret);
                        break;
                    case R.id.navigation_addTransaction:
                        active = secret;
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.content_main_frag, createTransactions);
                        transaction.commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.create_new_transaction_label);
                        break;
                }
                drawer.closeDrawers();

                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        user_iv = headerView.findViewById(R.id.user_iv);
        user_tv = headerView.findViewById(R.id.user_tv);


        try {
            setValues();
        } catch (URISyntaxException e) {
            Log.e(Global.TAG, e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {


            new AlertDialog.Builder(Global.context)
                    .setMessage("Do you really want to close application?")
                    .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AuthLogoutApiCall.Execute(new ActionCompleteListener() {
                                @Override
                                public void actionCompleted() {
                                    Intent intent = new Intent(Global.context, LoginActivity.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences(Global.MYPREFS, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor SPEditor = sharedPreferences.edit();
                                    SPEditor.clear().apply();
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void actionFailed(String error) {
                                    Log.e(Global.TAG, "Error: " + error);
                                }
                            });
                        }
                    })
                    .setNegativeButton(R.string.close_application, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNeutralButton(R.string.cancel, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.content_main_frag);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void setValues() throws URISyntaxException {
        Picasso.get()
                .load(String.valueOf(new URI(Global.SCHEME + Global.HOST + "/api/User/ProfileImage?user=" + Global.person.UserName)))
                .into(user_iv);
        user_tv.setText(Global.person.UserName);
    }
}