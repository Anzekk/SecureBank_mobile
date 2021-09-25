package com.secure_bank.bank.WebServices.Transaction.GetTransactions;

import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionGetTransactionsCompleteListener;
import com.secure_bank.bank.Model.TransactionRespDataTableResp;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class GetTransactionsApiCall extends AsyncTask<Void, Void, Void> {
    public static boolean running;
    private ActionGetTransactionsCompleteListener Listener;
    private TransactionRespDataTableResp response;
    private Integer start;
    private Integer length;
    private String search;

    private GetTransactionsApiCall(Integer start, Integer length, String search, ActionGetTransactionsCompleteListener listener) {
        this.Listener = listener;
        this.start = start;
        this.length = length;
        this.search = search;
    }

    public static void Execute(Integer start, Integer length, String search, ActionGetTransactionsCompleteListener listener) {
        if (running) {
            listener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        GetTransactionsApiCall loginAsync = new GetTransactionsApiCall(start, length, search, listener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink(Integer start, Integer length, String search) {
        String PATH = "/api/Transaction/GetTransactions?";
        String defaultParam = "start=%s&length=%s&search[regex]=%s&search[value]=%s";

        return Global.SCHEME + Global.HOST + PATH +
                String.format(defaultParam, start, length, false, (search));
    }

    public static TransactionRespDataTableResp call(Integer start, Integer length, String search) {

        TransactionRespDataTableResp transactionsResponseRecord = new TransactionRespDataTableResp();
        String link = getLink(start, length, search);
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
                return transactionsResponseRecord;
            }

            transactionsResponseRecord = TransactionRespDataTableResp.convertStringJsonToTransactionRespDAtaTableResp(response);

            return transactionsResponseRecord;

        } catch (Exception e) {
            e.printStackTrace();
            transactionsResponseRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            transactionsResponseRecord.Error = e.toString();
            return transactionsResponseRecord;
        }
    }

    protected void onPreExecute() {
        running = true;

        this.response = new TransactionRespDataTableResp();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(start, length, search);
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
            this.Listener.actionCompleted(response.Data);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
