package com.secure_bank.bank.Util;

import android.content.Context;

import com.secure_bank.bank.Model.StoreItem;
import com.secure_bank.bank.Model.TransactionResp;
import com.secure_bank.bank.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static String SCHEME = "http://";
    public static final String TAG = "SB";
    ///////////////////////////////////////////////////
    /////// PREFS
    public static final String MYPREFS = "SecureBankPrefs";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SESSION = "sessionToken";
    public static final String IP = "ip_host";
    public static final String PROTOCOL = "protocol_scheme";
    public static final int TIMEOUT = 1000 * 10;
    public static final int HTTP_OK_RESPONSE = 200;
    public static final int CONNECTIONTIMEOUT = 1000 * 10;
    public static String HOST = "";
    public static Context context;

    public static UserModel person;

    public static List<TransactionResp> transactions = new ArrayList<>();
    public static List<StoreItem> StoreItems = new ArrayList<>();
    public static List<String> searchItems = new ArrayList<>();
    public static boolean rootUser;

    public static class Error {
        public static final int ERROR_UNKNOWN = -9999;
        public static final int NO_INTERNET = -1337;
        public static final String NO_INTERNET_TEXT = "No connection available.";
        public static final String TASK_RUNNING = "Task already running.";
    }

}
