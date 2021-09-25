package com.secure_bank.bank.WebServices.Store.GetStoreItems;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionGetStoreItemsCompleteListener;
import com.secure_bank.bank.Model.ListStoreItemRecord;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class StoreGetStoreItemsApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionGetStoreItemsCompleteListener Listener;
    private ListStoreItemRecord response;

    private StoreGetStoreItemsApiCall(ActionGetStoreItemsCompleteListener listener) {
        this.Listener = listener;
    }

    public static void Execute(ActionGetStoreItemsCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        StoreGetStoreItemsApiCall loginAsync = new StoreGetStoreItemsApiCall(listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/api/Store/GetStoreItems";
        return Global.SCHEME + Global.HOST + PATH;
    }

    public static ListStoreItemRecord call() {
        ListStoreItemRecord storeItemRecord = new ListStoreItemRecord();
        String link = getLink();
        Log.e(Global.TAG, link);
        try {
            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "GET");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");
            restClient.AddHeader("Cookie", "SessionId=" + Global.person.Cookie);

            restClient.Execute(RestClient.RequestMethod.GET);
            String response = restClient.getResponse();

            if (response.length() <= 3) {
                return storeItemRecord;
            }

            storeItemRecord.data = ListStoreItemRecord.convertStringJsonToResponseRecord(response);

            return storeItemRecord;

        } catch (Exception e) {
            e.printStackTrace();
            storeItemRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            storeItemRecord.Error = e.toString();
            return storeItemRecord;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new ListStoreItemRecord();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call();
            } else {
                this.response.Error = Global.Error.NO_INTERNET_TEXT;
                this.response.ErrorCode = Global.Error.NO_INTERNET;
            }
        } catch (Exception e) {
            this.response.Error = e.toString();
            this.response.ErrorCode = Global.Error.ERROR_UNKNOWN;
        }
        return null;
    }

    protected void onPostExecute(Void voidR) {
        running = false;

        if (this.response.ErrorCode >= 0 && (this.response.Error != null && this.response.Error.isEmpty())) {
            this.Listener.actionCompleted(response.data);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
