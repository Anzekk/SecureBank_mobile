package com.secure_bank.bank.WebServices.Transaction.Get;

import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionGetTransactionCompleteListener;
import com.secure_bank.bank.Model.TransactionResp;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class TransactionGetApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionGetTransactionCompleteListener Listener;
    private TransactionResp response;
    private Integer index;

    private TransactionGetApiCall(Integer index, ActionGetTransactionCompleteListener listener) {
        this.Listener = listener;
        this.index = index;
    }

    public static void Execute(Integer index, ActionGetTransactionCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        TransactionGetApiCall loginAsync = new TransactionGetApiCall(index, listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink(Integer index) {
        String PATH = "/api/Transaction/Get/";
        String defaultParam = "%s";

        return Global.SCHEME + Global.HOST + PATH +
                String.format(defaultParam, index);
    }

    public static TransactionResp call(Integer index) {
        TransactionResp transactionDBRecord = new TransactionResp();
        String link = getLink(index);
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
                return transactionDBRecord;
            }

            transactionDBRecord = TransactionResp.convertStringJsonToResponseResp(response);

            return transactionDBRecord;

        } catch (Exception e) {
            e.printStackTrace();
            transactionDBRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            transactionDBRecord.Error = e.toString();
            return transactionDBRecord;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new TransactionResp();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(index);
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
            this.Listener.actionCompleted(response);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
