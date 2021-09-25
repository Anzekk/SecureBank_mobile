package com.secure_bank.bank.WebServices.Auth.PasswordRecovery;


import android.os.AsyncTask;
import android.util.Log;

import com.secure_bank.bank.Listener.ActionCompleteListener;
import com.secure_bank.bank.Model.EmptyResult;
import com.secure_bank.bank.Util.Global;
import com.secure_bank.bank.Util.Network;
import com.secure_bank.bank.WebServices.RestClient;

import org.json.JSONObject;

public class PasswordRecoveryApiCall extends AsyncTask<Void, Void, Void> {
    private static boolean running;
    private ActionCompleteListener Listener;
    private EmptyResult response;
    private String username;

    private PasswordRecoveryApiCall(String username, ActionCompleteListener actionCompleteListener) {
        this.Listener = actionCompleteListener;
        this.username = username;
    }

    public static void Execute(String username, ActionCompleteListener actionCompleteListener) {
        if (running) {
            actionCompleteListener.actionFailed(Global.Error.TASK_RUNNING);
            return;
        }
        PasswordRecoveryApiCall loginAsync = new PasswordRecoveryApiCall(username, actionCompleteListener);
        loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static String getLink() {
        String PATH = "/api/Auth/PasswordRecovery";

        return Global.SCHEME + Global.HOST + PATH;
    }

    public static EmptyResult call(String username) {
        EmptyResult emptyResponseRecord = new EmptyResult();
        String link = getLink();
        Log.e(Global.TAG, link);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("userName", username);

            final String requestBody = jsonBody.toString();

            RestClient restClient = new RestClient(link);
            restClient.AddHeader("Method", "POST");
            restClient.AddHeader("Accept-Encoding", "gzip");
            restClient.AddHeader("Accept", "application/json");
            restClient.AddHeader("Content-type", "application/json");

            restClient.SetBody(requestBody);


            restClient.Execute(RestClient.RequestMethod.POST);
            String response = restClient.getResponse();

            if (response.length() <= 3) {
                return emptyResponseRecord;
            }

            emptyResponseRecord = EmptyResult.convertStringJsonToEmptyResult(response);

            return emptyResponseRecord;

        } catch (Exception e) {
            e.printStackTrace();
            emptyResponseRecord.ErrorCode = Global.Error.ERROR_UNKNOWN;
            emptyResponseRecord.Error = e.toString();
            return emptyResponseRecord;
        }
    }

    protected void onPreExecute() {
        running = true;
        this.response = new EmptyResult();
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
            this.Listener.actionCompleted();
        } else {
            this.Listener.actionFailed(String.format("ActionFailed at %s: Code=[%s] (%s)", this.getClass().getSimpleName(), response.ErrorCode, response.Error));
        }
    }
}
