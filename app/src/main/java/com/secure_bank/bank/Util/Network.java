package com.secure_bank.bank.Util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class Network {
    public static boolean hasConnection() {
        boolean mobileData = false, WiFi = false;

        for (NetworkInfo networkInfo : ((ConnectivityManager) Objects.requireNonNull(Global.context.getSystemService(Context.CONNECTIVITY_SERVICE))).getAllNetworkInfo()) {

            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE") && networkInfo.isConnected()) {
                mobileData = true;
            }
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI") && networkInfo.isConnected()) {
                WiFi = true;
            }
        }
        return WiFi || mobileData;
    }
}

