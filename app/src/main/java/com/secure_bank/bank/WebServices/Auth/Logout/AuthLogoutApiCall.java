package com.secure_bank.bank.WebServices.Auth.Logout;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.EmptyResult;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

public class AuthLogoutApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionCompleteListener Listener;
    private EmptyResult response;

    private AuthLogoutApiCall(ActionCompleteListener actionCompleteListener) {
        this.Listener = actionCompleteListener;
    }

    public static void Execute(ActionCompleteListener actionCompleteListener) {
        if (running) {
            actionCompleteListener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        AuthLogoutApiCall loginAsync = new AuthLogoutApiCall(actionCompleteListener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/api/Auth/Logout";
        return Global.SCHEME + Global.HOST + PATH;
    }

    public static EmptyResult call() {
        EmptyResult EmptyResult = new EmptyResult();
        String link = getLink();
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
                return EmptyResult;
            }

            EmptyResult = EmptyResult.convertStringJsonToEmptyResult(response);

            return EmptyResult;

        } catch (Exception e) {
            e.printStackTrace();
            EmptyResult.ErrorCode = Global.Error.ERROR_UNKNOWN;
            EmptyResult.Error = e.toString();
            return EmptyResult;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new EmptyResult();
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
            this.Listener.actionCompleted();
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
