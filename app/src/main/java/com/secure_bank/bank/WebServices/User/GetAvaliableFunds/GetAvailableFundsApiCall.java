package com.secure_bank.bank.WebServices.User.GetAvaliableFunds;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionIntegerCompleteListener;
import com.secure_bank.bank.Model.AccountBalanceResp;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class GetAvailableFundsApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionIntegerCompleteListener Listener;
    private AccountBalanceResp response;
    private String username;

    private GetAvailableFundsApiCall(String username, ActionIntegerCompleteListener actionCompleteListener) {
        this.Listener = actionCompleteListener;
        this.username = username;
    }

    public static void Execute(String username, ActionIntegerCompleteListener actionCompleteListener) {
        if (running) {
            actionCompleteListener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        GetAvailableFundsApiCall loginAsync = new GetAvailableFundsApiCall(username, actionCompleteListener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink(String username) {
        String PATH = "/api/User/GetAvailableFunds?";

        String defaultParam = "user=%s";

        return Global.SCHEME + Global.HOST + PATH +
                String.format(defaultParam, username);
    }

    public static AccountBalanceResp call(String username) {

        AccountBalanceResp getAvailableFundsResponseRecord = new AccountBalanceResp();
        String link = getLink(username);
        Log.e(Global.TAG, link);
        try {
            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "GET");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");

            restClient.Execute(RestClient.RequestMethod.GET);
            String response = restClient.getResponse();

            if (response.length() <= 3) {
                return getAvailableFundsResponseRecord;
            }

            getAvailableFundsResponseRecord = AccountBalanceResp.convertStringJsonToAccountBalanceResp(response);

            return getAvailableFundsResponseRecord;

        } catch (Exception e) {
            e.printStackTrace();
            getAvailableFundsResponseRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            getAvailableFundsResponseRecord.Error = e.toString();
            return getAvailableFundsResponseRecord;
        }

    }

    protected void onPreExecute() {
        running = true;
        this.response = new AccountBalanceResp();
    }

    protected Void doInBackground(Void... voidArr) {
        try {
            if (Network.hasConnection()) {
                this.response = call(username);
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
            this.Listener.actionCompleted(response.Balance);
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
